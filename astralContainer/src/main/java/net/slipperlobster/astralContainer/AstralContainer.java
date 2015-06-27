/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

public class AstralContainer
{
    private Map<AstralTypeId, AstralType> _factories = new HashMap<>();

    public void register(Class<?> interfaceClass, Class<?> implClass) {
        registerImpl(interfaceClass, null, new AstralType(implClass));
    }

    public void register(TypeRef<?> interfaceClass, Class<?> implClass) {
        registerImpl(interfaceClass.getType(), null, new AstralType(implClass));
    }

    public void register(Class<?> interfaceClass, TypeRef<?> implClass) {
        registerImpl(interfaceClass, null, new AstralType(implClass));
    }

    public void register(TypeRef<?> interfaceClass, TypeRef<?> implClass) {
        registerImpl(interfaceClass.getType(), null, new AstralType(implClass));
    }

    public void register(Class<?> interfaceClass, String qualifier, Class<?> implClass) {
        registerImpl(interfaceClass, qualifier, new AstralType(implClass));
    }

    public void register(TypeRef<?> interfaceClass, String qualifier, Class<?> implClass) {
        registerImpl(interfaceClass.getType(), qualifier, new AstralType(implClass));
    }

    public void register(Class<?> interfaceClass, String qualifier, TypeRef<?> implClass) {
        registerImpl(interfaceClass, qualifier, new AstralType(implClass));
    }

    public void register(TypeRef<?> interfaceClass, String qualifier, TypeRef<?> implClass) {
        registerImpl(interfaceClass.getType(), qualifier, new AstralType(implClass));
    }

    public <T> void register(Class<T> interfaceClass, FactoryFunction<T> ctor) {
        registerImpl(interfaceClass, null, new AstralType(interfaceClass, new InjectedFactory(ctor)));
    }

    public <T> void registerSingletonInstance(TypeRef<?> interfaceClass, T instance) {
        registerSingletonInstanceImpl(interfaceClass.getType(), null, instance);
    }

    public <T> void registerSingletonInstance(Class<?> interfaceClass, T instance) {
        registerSingletonInstanceImpl(interfaceClass, null, instance);
    }

    public <T> void registerSingletonInstance(TypeRef<?> interfaceClass, String qualifier, T instance) {
        registerSingletonInstanceImpl(interfaceClass.getType(), qualifier, instance);
    }

    public <T> void registerSingletonInstance(Class<?> interfaceClass, String qualifier, T instance) {
        registerSingletonInstanceImpl(interfaceClass, qualifier, instance);
    }

    public <T> T resolve(Class<T> interfaceClass) throws Exception {
        return resolve((Type) interfaceClass, null);
    }

    public <T> T resolve(TypeRef<T> interfaceClass) throws Exception {
        return resolve(interfaceClass.getType(), null);
    }

    public <T> T resolve(Class<T> interfaceClass, String qualifier) throws Exception {
        return resolve((Type) interfaceClass, qualifier);
    }

    public <T> T resolve(TypeRef<T> interfaceClass, String qualifier) throws Exception {
        return resolve(interfaceClass.getType(), qualifier);
    }

    public void validate() throws AstralException {
        Set<AstralTypeId> visited = new HashSet<>(_factories.size());
        Deque<AstralTypeId> nodePath = new ArrayDeque<>(_factories.size() / 2);
        for (Map.Entry<AstralTypeId, AstralType> entry : _factories.entrySet()) {
            visited.clear();
            nodePath.clear();
            validateRecursive(entry.getKey(), entry.getValue(), visited, nodePath);
        }
    }

    public <T> void registerSingletonInstanceImpl(Type interfaceClass, String qualifier, T instance) {
        AstralFactory factory = new SingletonInstanceFactory(instance);
        AstralType astralType = new AstralType(instance.getClass(), qualifier, factory);
        registerImpl(interfaceClass, qualifier, astralType);
    }

    private void registerImpl(Type interfaceClass, String qualifier, AstralType implClassRef) {
        AstralTypeId key = new AstralTypeId(interfaceClass, qualifier);
        _factories.put(key, implClassRef);
    }

    private <T> T resolve(Type interfaceClass, String qualifier) throws Exception {
        return resolve(new AstralTypeId(interfaceClass, qualifier));
    }

    private <T> T resolve(AstralTypeId typeKey) throws Exception {
        AstralType typeNode = _factories.get(typeKey);
        if (typeNode == null)
            throw createUnregisteredTypeException(typeKey, null, null);

        Set<AstralTypeId> visited = new HashSet<>(_factories.size());
        Deque<AstralTypeId> nodePath = new ArrayDeque<>(_factories.size() / 2);
        //noinspection unchecked
        return (T) resolveRecursive(typeKey, typeNode, visited, nodePath);
    }

    private Object resolveRecursive(AstralTypeId requestedTypeId, AstralType typeNode, Set<AstralTypeId> visited, Deque<AstralTypeId> nodePath)
            throws Exception {
        visited.add(requestedTypeId);
        nodePath.push(requestedTypeId);
        nodePath.push(typeNode.getTypeId());

        AstralTypeId[] dependentTypeIds = typeNode.getDependentTypeIds();
        ParameterList dependentObjects = new ParameterList(dependentTypeIds.length);
        for (AstralTypeId dependentTypeId : dependentTypeIds) {
            AstralType dependentTypeNode = _factories.get(dependentTypeId);
            if (dependentTypeNode == null)
                throw createUnregisteredTypeException(dependentTypeId, typeNode, nodePath);
            if (visited.contains(dependentTypeId))
                throw createCircularDependencyException(typeNode, dependentTypeId, nodePath);
            Object dependentObject = resolveRecursive(dependentTypeId, dependentTypeNode, visited, nodePath);
            dependentObjects.add(dependentTypeId, dependentObject);
        }

        nodePath.pop();
        nodePath.pop();
        return typeNode.getFactory().create(typeNode, dependentObjects, this);
    }


    private void validateRecursive(AstralTypeId requestedTypeId, AstralType typeNode, Set<AstralTypeId> visited, Deque<AstralTypeId> nodePath)
            throws AstralException {
        visited.add(requestedTypeId);
        nodePath.push(requestedTypeId);
        nodePath.push(typeNode.getTypeId());

        for (AstralTypeId dependentTypeId : typeNode.getDependentTypeIds()) {
            AstralType dependentTypeNode = _factories.get(dependentTypeId);
            if (dependentTypeNode == null)
                throw createUnregisteredTypeException(dependentTypeId, typeNode, nodePath);
            if (visited.contains(dependentTypeId))
                throw createCircularDependencyException(typeNode, dependentTypeId, nodePath);

            validateRecursive(dependentTypeId, dependentTypeNode, visited, nodePath);
        }

        nodePath.pop();
        nodePath.pop();
    }

    private AstralException createUnregisteredTypeException(AstralTypeId typeId,
                                                            AstralType referencingType,
                                                            Deque<AstralTypeId> nodePath) {
        StringBuilder stringBuilder = new StringBuilder("Attempted to create unregistered type: ");
        stringBuilder.append(typeId.getTypeName());
        if (referencingType != null) {
            stringBuilder.append(" while constructing: ");
            stringBuilder.append(referencingType);
        }
        addPathInfo(stringBuilder, nodePath, typeId);
        return new AstralException(stringBuilder.toString());
    }

    private AstralException createCircularDependencyException(AstralType typeNode,
                                                              AstralTypeId dependentTypeId,
                                                              Deque<AstralTypeId> nodePath) {
        StringBuilder stringBuilder = new StringBuilder("Circular reference detected for type: ");
        stringBuilder.append(typeNode.getTypeName());
        stringBuilder.append(" referencing: ");
        stringBuilder.append(dependentTypeId.getTypeName());
        addPathInfo(stringBuilder, nodePath, dependentTypeId);
        return new AstralException(stringBuilder.toString());
    }

    private void addPathInfo(
            StringBuilder stringBuilder,
            Deque<AstralTypeId> nodePath,
            AstralTypeId finalTypeId) {
        if (nodePath != null) {
            stringBuilder.append(" through from path: ");
            Iterator<AstralTypeId> iterator = nodePath.descendingIterator();
            while (iterator.hasNext()) {
                AstralTypeId nodeTypeId = iterator.next();
                stringBuilder.append(nodeTypeId.getTypeName());
                stringBuilder.append(" -> ");
            }
            stringBuilder.append(finalTypeId.getTypeName());
        }
    }

}