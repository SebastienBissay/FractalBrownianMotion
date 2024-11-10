import processing.core.PApplet;
import processing.core.PVector;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class FractalBrownianMotion extends PApplet {
    public static void main(String[] args) {
        PApplet.main(FractalBrownianMotion.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        noLoop();
    }

    @Override
    public void draw() {
        loadPixels();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                PVector p = new PVector(i * NOISE_SCALE, j * NOISE_SCALE);
                PVector q = new PVector(fbm(p), fbm(PVector.add(FBM_VECTOR_1, p)));
                PVector r = new PVector(fbm(PVector.add(FBM_VECTOR_2, p).add(PVector.mult(q, FBM_MULTIPLIER))),
                        fbm(PVector.add(FBM_VECTOR_3, p).add(PVector.mult(q, FBM_MULTIPLIER))));
                float t = fbm(PVector.add(p, PVector.mult(r, FBM_MULTIPLIER)));
                pixels[i + width * j] = lerpColor(PALETTE[floor(t * (PALETTE.length)) % PALETTE.length].toInt(),
                        PALETTE[(floor(t * (PALETTE.length) + 1)) % PALETTE.length].toInt(),
                        t * PALETTE.length % 1);
            }
        }
        updatePixels();
        saveSketch(this);
    }

    private float fbm(PVector p) {
        return fbm(p.x, p.y);
    }

    private float fbm(float x, float y) {
        float n = 0;
        float f = 1;
        float a = 1;
        float g = .5f;
        for (int k = 0; k < 3; k++) {
            n += a * noise(f * x, f * y);
            f *= 2.;
            a *= g;
        }
        return n;
    }

    float H(int n) {
        float r = 0;
        int m = n;
        while (m >= 0) {
            r += pow(2, -m);
            m--;
        }
        return r;
    }
}
