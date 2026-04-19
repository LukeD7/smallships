package com.talhanation.smallships.world.entity.ship;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class Attributes {
    public float maxHealth;
    public float maxSpeed;
    public float maxReverseSpeed;
    public float maxRotationSpeed;
    public float acceleration;
    public float rotationAcceleration;
    public float friction;

    public void addSaveData(CompoundTag tag) {
        CompoundTag compoundtag = new CompoundTag();
        compoundtag.putFloat("maxHealth", this.maxHealth);
        compoundtag.putFloat("maxSpeed", this.maxSpeed);
        compoundtag.putFloat("maxReverseSpeed", this.maxReverseSpeed);
        compoundtag.putFloat("acceleration", this.acceleration);
        compoundtag.putFloat("rotationAcceleration", this.rotationAcceleration);
        compoundtag.putFloat("maxRotationSpeed", this.maxRotationSpeed);
        compoundtag.putFloat("friction", this.friction);
        tag.put("Attributes", compoundtag);
    }

    public void addSaveData(ValueOutput output) {
        ValueOutput child = output.child("Attributes");
        child.putFloat("maxHealth", this.maxHealth);
        child.putFloat("maxSpeed", this.maxSpeed);
        child.putFloat("maxReverseSpeed", this.maxReverseSpeed);
        child.putFloat("acceleration", this.acceleration);
        child.putFloat("rotationAcceleration", this.rotationAcceleration);
        child.putFloat("maxRotationSpeed", this.maxRotationSpeed);
        child.putFloat("friction", this.friction);
    }

    public CompoundTag getSaveData() {
        CompoundTag compoundtag = new CompoundTag();
        this.addSaveData(compoundtag);
        return compoundtag;
    }

    public void loadSaveData(CompoundTag tag) {
        if (tag.contains("Attributes")) {
            CompoundTag compoundtag = tag.getCompound("Attributes").orElseThrow();
            this.maxHealth = compoundtag.getFloat("maxHealth").orElseThrow();
            this.maxSpeed = compoundtag.getFloat("maxSpeed").orElseThrow();
            this.maxReverseSpeed = compoundtag.getFloat("maxReverseSpeed").orElseThrow();
            this.acceleration = compoundtag.getFloat("acceleration").orElseThrow();
            this.rotationAcceleration = compoundtag.getFloat("rotationAcceleration").orElseThrow();
            this.maxRotationSpeed = compoundtag.getFloat("maxRotationSpeed").orElseThrow();
            this.friction = compoundtag.getFloat("friction").orElseThrow();
        }
    }

    public void loadSaveData(ValueInput input) {
        input.child("Attributes").ifPresent(child -> {
            this.maxHealth = child.getFloatOr("maxHealth", 0);
            this.maxSpeed = child.getFloatOr("maxSpeed", 0);
            this.maxReverseSpeed = child.getFloatOr("maxReverseSpeed", 0);
            this.acceleration = child.getFloatOr("acceleration", 0);
            this.rotationAcceleration = child.getFloatOr("rotationAcceleration", 0);
            this.maxRotationSpeed = child.getFloatOr("maxRotationSpeed", 0);
            this.friction = child.getFloatOr("friction", 0);
        });
    }

    public void loadSaveData(CompoundTag tag, Ship shipEntity) {
        if (tag.contains("Attributes")) {
            this.loadSaveData(tag);
        } else {
            this.loadSaveData(shipEntity.createDefaultAttributes());
        }
    }

    public void loadSaveData(ValueInput input, Ship shipEntity) {
        if (input.child("Attributes").isPresent()) {
            this.loadSaveData(input);
        } else {
            this.loadSaveData(shipEntity.createDefaultAttributes());
        }
    }

    @Override
    public String toString() {
        return "Attributes{" +
                "maxHealth=" + maxHealth +
                ", maxSpeed=" + maxSpeed +
                ", maxReverseSpeed=" + maxReverseSpeed +
                ", acceleration=" + acceleration +
                ", maxRotationSpeed=" + maxRotationSpeed +
                ", friction=" + friction +
                '}';
    }
}
