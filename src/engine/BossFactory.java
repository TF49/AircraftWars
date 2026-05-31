package engine;

import model.Aircraft;
import model.Boss1;
import model.Boss2;

public class BossFactory {

    public static Aircraft create(int checkpoint) {
        if (checkpoint % 2 == 1) {
            return new Boss1(checkpoint);
        }
        return new Boss2(checkpoint);
    }
}
