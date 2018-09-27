package com.tw.relife;

import java.util.Objects;

public class RelifeAction {

    private String path;
    private RelifeMethod method;

    public RelifeAction(String path, RelifeMethod method) {
        this.path = path;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public RelifeMethod getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelifeAction that = (RelifeAction) o;
        return Objects.equals(path, that.path) &&
                method == that.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}
