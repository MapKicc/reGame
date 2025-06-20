// Game manager

// Game manager class
public class Game {
    // Game score
    public static int score = 0;

    // Current selections
    public static String currentSprite = "sprite1";
    public static int currentPhase = 1;

    // Phase unlocks
    public static boolean phase1 = true;
    public static boolean phase2 = false;
    public static boolean phase3 = false;

    // Phase settings
    public static int phase1Items = 10;
    public static int phase2Items = 15;
    public static int phase3Items = 20;
    public static int phase1Time = 240;
    public static int phase2Time = 180;
    public static int phase3Time = 120;

    // Get items for current phase
    public static int getItems() {
        if (currentPhase == 1) {
            return phase1Items;
        } else if (currentPhase == 2) {
            return phase2Items;
        } else if (currentPhase == 3) {
            return phase3Items;
        } else {
            return 0;
        }
    }

    // Get timer for current phase
    public static int getTime() {
        if (currentPhase == 1) {
            return phase1Time;
        } else if (currentPhase == 2) {
            return phase2Time;
        } else if (currentPhase == 3) {
            return phase3Time;
        } else {
            return 0;
        }

    }

    // Unlock next phase
    public static void unlockPhase() {
        if (currentPhase == 1) {
            phase2 = true;
        } else if (currentPhase == 2) {
            phase3 = true;
        }
    }

    // Go to next phase
    public static void nextPhase() {
        currentPhase++;
        if (currentPhase == 2) {
            phase2 = true;
        } else if (currentPhase == 3) {
            phase3 = true;
        }
    }
}