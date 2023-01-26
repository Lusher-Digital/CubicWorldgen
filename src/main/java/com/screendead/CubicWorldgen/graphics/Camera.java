package com.screendead.CubicWorldgen.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

@SuppressWarnings("unused")
public class Camera {
    public static final float SPEED = 0.005f;
    public static final float SPEED_RUN = 0.015f;
    private static final float HORZ_SENSITIVITY = 0.15f;
    private static final float VERT_SENSITIVITY = 0.1f;

    private final Vector3f position;
    private final Vector3f velocity;
    private final Vector3f acceleration;

    private float horizontalAngle; // degrees
    private float verticalAngle; // degrees
    private float fov;
    private float aspectRatio;
    private float nearPlane;
    private float farPlane;
    private boolean running = false;

    public Camera(Vector3f position, float horizontalAngle, float verticalAngle, float fov, float aspectRatio, float nearPlane, float farPlane) {
        this.position = position;
        this.velocity = new Vector3f();
        this.acceleration = new Vector3f();
        this.horizontalAngle = horizontalAngle;
        this.verticalAngle = verticalAngle;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getLook() {
        return new Vector3f(
                (float) (Math.cos(Math.toRadians(horizontalAngle)) * Math.cos(Math.toRadians(verticalAngle))),
                (float) Math.sin(Math.toRadians(verticalAngle)),
                (float) (Math.sin(Math.toRadians(horizontalAngle)) * Math.cos(Math.toRadians(verticalAngle)))
        ).normalize();
    }

    public Matrix4f getViewMatrix() {
        Vector3f direction = getLook();

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

        acceleration.add(movement.normalize(running ? SPEED_RUN : SPEED));
    }

    public void update(int ticksToComplete) {
        for (int i = 0; i < ticksToComplete; i++)
            update();
    }

    public void update() {
        velocity.add(acceleration);
        position.add(velocity);
        acceleration.zero();
        velocity.mul(0.9f);

        if (velocity.length() < 0.0001f)
            velocity.zero();
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

    public void setRunning(boolean running) {
        this.running = running;
    }

    public float getNear() {
        return nearPlane;
    }

    public float getFar() {
        return farPlane;
    }
}
