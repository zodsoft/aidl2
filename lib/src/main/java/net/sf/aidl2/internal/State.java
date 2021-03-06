package net.sf.aidl2.internal;

import com.squareup.javapoet.NameAllocator;
import net.sf.aidl2.internal.util.NullStream;

import javax.lang.model.type.TypeMirror;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class State implements Cloneable {
    private final AidlProcessor.Environment environment;

    NameAllocator allocator;
    boolean external;
    boolean allowUnchecked;
    boolean nullable = true;
    boolean returnValue;
    boolean assumeFinal;
    TypeMirror type;
    CharSequence name;
    ContractHasher digest;

    public State(AidlProcessor.Environment environment, NameAllocator allocator, ContractHasher digest) {
        this.environment = environment;
        this.allocator = allocator;
        this.digest = digest;
    }

    public State allowUnchecked(boolean allowUnchecked) {
        this.allowUnchecked = allowUnchecked;
        return this;
    }

    public State setParameter(AidlParamModel param) {
        this.returnValue = param.isReturn();
        this.name = param.name == null ? "returnValue" : allocator.get(param);
        this.type = param.type;
        this.nullable = param.nullable;
        return this;
    }

    public State assumeFinal(boolean assumeFinal) {
        this.assumeFinal = assumeFinal;
        return this;
    }

    public State external(boolean external) {
        this.external = external;
        return this;
    }

    public DataOutputStream versionCalc() {
        return digest;
    }

    @Override
    @SuppressWarnings("all")
    public State clone() {
        try {
            State state = (State) super.clone();

            state.allocator = allocator.clone();

            return state;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public Reader buildReader(String name) {
        return new Reader(environment, this, name);
    }

    public Writer buildWriter(String name) {
        return new Writer(environment, this, name);
    }
}
