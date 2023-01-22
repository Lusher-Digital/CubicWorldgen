package com.screendead.CubicWorldgen.graphics;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@SuppressWarnings("unused")
public class Camera {
    private final Vector3f position;
    private final Vector3f look;
    private final Vector3f up;
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

    public Camera(Vector3f position, Vector3f look, float fov, float aspectRatio, float nearPlane, float farPlane) {
        this.position = position;
        this.look = look;
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, new Vector3f(position).add(look), up);
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    public void move(Vector3f offset) {
        position.add(offset);
    }

    public void rotate(Quaternionf rotation) {
        look.rotate(rotation);
        up.rotate(rotation);
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

    public void setNearPlane(float nearPlane) {
        this.nearPlane = nearPlane;
    }

    public void setFarPlane(float farPlane) {
        this.farPlane = farPlane;
    }
}
