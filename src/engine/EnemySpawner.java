package engine;

import data.Data;
import model.Aircraft;
import model.Enemy;

import java.util.List;
import java.util.Random;

public class EnemySpawner {

    public static void spawn(List<Aircraft> enemy, int fps, int checkpoint, Random random) {
        int[] hp = LevelConfig.computeHp(checkpoint, fps);
        int pattern = random.nextInt(10) + 1;
        spawnPattern(enemy, pattern, hp, random);
    }

    static void spawnPattern(List<Aircraft> enemy, int pattern, int[] hp, Random random) {
        switch (pattern) {
            case 1:
                enemy.add(new Enemy(Data.enemyAircraftImages[4], Data.enemyDeathImages[4], 2, 90, random.nextInt(300) + 100, -150, hp[4]));
                break;
            case 2:
                enemy.add(new Enemy(Data.enemyAircraftImages[3], Data.enemyDeathImages[3], 2, 90, 100, -150, hp[3]));
                enemy.add(new Enemy(Data.enemyAircraftImages[3], Data.enemyDeathImages[3], 2, 90, 380, -150, hp[3]));
                break;
            case 3:
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 90, 10, -150, hp[2]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 90, 240, -150, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 90, 530, -150, hp[2]));
                break;
            case 4:
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 90, 0, -150, hp[2]));
                enemy.add(new Enemy(Data.enemyAircraftImages[3], Data.enemyDeathImages[3], 2, 90, 130, -150, hp[3]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 90, 300, -150, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 90, 450, -150, hp[1]));
                break;
            case 5:
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 110, 400, random.nextInt(100) - 200, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 110, 480, random.nextInt(100) - 200, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 110, 560, random.nextInt(100) - 200, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 110, 640, random.nextInt(100) - 200, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 110, 720, random.nextInt(100) - 200, hp[0]));
                break;
            case 6:
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 70, -440, random.nextInt(100) - 200, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 70, -320, random.nextInt(100) - 200, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 70, -240, random.nextInt(100) - 200, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[0], Data.enemyDeathImages[0], 2, 70, -170, random.nextInt(100) - 200, hp[0]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 70, -100, random.nextInt(100) - 200, hp[1]));
                break;
            case 7:
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 70, 30, random.nextInt(100) - 200, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 70, 100, random.nextInt(100) - 200, hp[2]));
                enemy.add(new Enemy(Data.enemyAircraftImages[3], Data.enemyDeathImages[3], 2, 70, 200, random.nextInt(100) - 200, hp[3]));
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 70, 300, random.nextInt(100) - 200, hp[2]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 70, 430, random.nextInt(100) - 200, hp[1]));
                break;
            case 8:
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 30, -100, -150, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 90, 240, -150, hp[1]));
                enemy.add(new Enemy(Data.enemyAircraftImages[1], Data.enemyDeathImages[1], 2, 120, 715, -150, hp[1]));
                break;
            case 9:
                enemy.add(new Enemy(Data.enemyAircraftImages[4], Data.enemyDeathImages[4], 2, 90, 100 + random.nextInt(100), -150, hp[4]));
                enemy.add(new Enemy(Data.enemyAircraftImages[4], Data.enemyDeathImages[4], 2, 90, 415 - random.nextInt(100), -150, hp[4]));
                break;
            case 10:
            default:
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 90, 120 + random.nextInt(100), -150, hp[2]));
                enemy.add(new Enemy(Data.enemyAircraftImages[2], Data.enemyDeathImages[2], 2, 90, 395 - random.nextInt(100), -150, hp[2]));
                break;
        }
    }
}
