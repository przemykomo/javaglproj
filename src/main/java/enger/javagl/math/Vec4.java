package enger.javagl.math;

public class Vec4 extends Vec3 {
    public double w;

    public Vec4(double x, double y, double z, double w) {
        super(x, y, z);
        this.w = w;
    }
}
