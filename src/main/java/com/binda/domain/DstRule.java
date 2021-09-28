package com.binda.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Component
@Entity
@Table(name = "DST_RULE")
public class DstRule implements Serializable {
    private static final long serialVersionUID = 5286176551463512740L;
    @Id
    private int id;
    @Order(value = 1)
    @Size(min = 1, max = 40)
    @javax.validation.constraints.NotNull
    @NotNull
    private String name;

    public DstRule(int id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public DstRule() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DstRule dstRule = (DstRule) o;
        return id == dstRule.id && name.equals(dstRule.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
