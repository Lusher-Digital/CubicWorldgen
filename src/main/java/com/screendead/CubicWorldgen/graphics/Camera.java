package com.screendead.CubicWorldgen.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

@SuppressWarnings("unused")
public class Camera {
    public static final float SPEED = 0.1f;
    private static final float HORZ_SENSITIVITY = 0.1f;
    private static final float VERT_SENSITIVITY = 0.1f;

    private final Vector3f position;
    private float horizontalAngle; // degrees
    private float verticalAngle; // degrees
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;

    public Camera(Vector3f position, float horizontalAngle, float verticalAngle, float fov, float aspectRatio, float nearPlane, float farPlane) {
        this.position = position;
        this.horizontalAngle = horizontalAngle;
        this.verticalAngle = verticalAngle;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public Matrix4f getViewMatrix() {
        Vector3f direction = new Vector3f(
                (float) (Math.cos(Math.toRadians(horizontalAngle)) * Math.cos(Math.toRadians(verticalAngle))),
                (float) Math.sin(Math.toRadians(verticalAngle)),
                (float) (Math.sin(Math.toRadians(horizontalAngle)) * Math.cos(Math.toRadians(verticalAngle)))
        ).normalize();

        Vector3f right = new Vector3f(direction).cross(new Vector3f(0.0f, 1.0f, 0.0f)).normalize();
        Vector3f up = new Vector3f(right).cross(direction).normalize();

        return new Matrix4f()
                .lookAt(position, new Vector3f(position).add(direction), up);
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().perspective((float) Math.toRadians(fov), aspectRatio, nearPlane, farPlane);
    }

    public void move(float x, float y, float z) {
        Vector3f direction = new Vector3f(
                (float) (Math.cos(Math.toRadians(horizontalAngle)) * Math.cos(Math.toRadians(verticalAngle))),
                0.0f,
                (float) (Math.sin(Math.toRadians(horizontalAngle)) * Math.cos(Math.toRadians(verticalAngle)))
        ).normalize();

        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f right = new Vector3f(direction).cross(up).normalize();

        Vector3f movement = new Vector3f(direction).mul(z)
                .add(new Vector3f(right).mul(x))
                .add(new Vector3f(up).mul(y));

        position.add(movement.normalize(SPEED));
    }

    public void rotate(float dx, float dy) {
        horizontalAngle += dx * HORZ_SENSITIVITY;
        horizontalAngle %= 360.0f;
        verticalAngle += -dy * VERT_SENSITIVITY;
        verticalAngle = Math.max(-89.99f, Math.min(verticalAngle, 89.99f));
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

    public void setAspect(float aspect) {
        this.aspectRatio = aspect;
    }
}
