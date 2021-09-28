/*
 * Copyright (c) 2019, Sabre Holdings. All Rights Reserved.
 */

package com.binda.domain;

import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;


@Component
public class TimeZone implements Serializable {
    private static final long serialVersionUID = 5286176551463515740L;

    private int id;

    @Order(value = 1)
    @Size(max = 32)
    @Nullable
    private String name;

    @Order(value = 2)
    @Size(min = 2, max = 4)
    @NotNull
    private String code;

    public TimeZone(@Nullable String name, String code, int id) {
        this.name = name;
        this.code = code;
        this.id = id;
    }

    public TimeZone() {
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeZone timeZone = (TimeZone) o;
        return id == timeZone.id && Objects.equals(name, timeZone.name) && Objects.equals(code, timeZone.code);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }
}