package com.bitdecay.game.menu;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bitdecay.game.Helm;
import com.bitdecay.game.unlock.stats.StatName;
import com.bitdecay.game.world.LevelInstance;

/**
 * Created by Monday on 4/9/2017.
 */

public class MedalUtils {
    private static TextureRegion bronzeMedalImg;
    private static TextureRegion silverMedalImg;
    private static TextureRegion goldMedalImg;
    private static TextureRegion devMedalImg;

    public static int imageSize;

    public enum LevelRating {
        UNRANKED(StatName.NO_STAT),
        BRONZE(StatName.BRONZE_MEDALS),
        SILVER(StatName.SILVER_MEDALS),
        GOLD(StatName.GOLD_MEDALS),
        DEV(StatName.DEV_MEDALS);


        private StatName statName;

        LevelRating(StatName statName) {
            this.statName = statName;
        }

        public StatName statName() {
            return statName;
        }
    }

    public static void init(Helm game) {
        bronzeMedalImg = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("bronze_medal");
        silverMedalImg = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("silver_medal");
        goldMedalImg = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("gold_medal");
        devMedalImg = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("dev_medal");

        imageSize = (int) (game.fontScale * 16);
    }

    public static Image getIconForHighScore(LevelInstance level) {
        LevelRating rank = getScoreRank(level, level.getHighScore());
        return getRankImage(rank);
    }

    public static Image getIconForScore(LevelInstance level, int score) {
        LevelRating rank = getScoreRank(level, score);
        return getRankImage(rank);
    }

    public static Image getIconForBestTime(LevelInstance level) {
        LevelRating rank = getTimeRank(level, level.getBestTime());
        return getRankImage(rank);
    }

    public static Image getIconForBestTime(LevelInstance level, float time) {
        LevelRating rank = getTimeRank(level, time);
        return getRankImage(rank);
    }


    public static LevelRating getScoreRank(LevelInstance level, int score) {
        if (score >= level.levelDef.devScore) {
            return LevelRating.DEV;
        } else if (score >= level.levelDef.goldScore) {
            return LevelRating.GOLD;
        } else if (score >= level.levelDef.silverScore) {
            return LevelRating.SILVER;
        } else if (score >= level.levelDef.bronzeScore) {
            return LevelRating.BRONZE;
        } else {
            return LevelRating.UNRANKED;
        }
    }

    public static LevelRating getTimeRank(LevelInstance level, float time) {
        // we do this so that float precision doesn't mess with us.
        int userTime = (int) (time * 1000);
        int devTime = (int) (level.levelDef.devTime * 1000);
        int goldTime = (int) (level.levelDef.goldTime * 1000);
        int silverTime = (int) (level.levelDef.silverTime * 1000);
        int bronzeTime = (int) (level.levelDef.bronzeTime * 1000);
        if (userTime <= devTime) {
            return LevelRating.DEV;
        } else if (userTime <= goldTime) {
            return LevelRating.GOLD;
        } else if (userTime <= silverTime) {
            return LevelRating.SILVER;
        } else if (userTime <= bronzeTime) {
            return LevelRating.BRONZE;
        } else {
            return LevelRating.UNRANKED;
        }
    }

    public static Image getRankImage(LevelRating rank) {
        switch(rank) {
            case UNRANKED:
                return new Image();
            case BRONZE:
                return new Image(bronzeMedalImg);
            case SILVER:
                return new Image(silverMedalImg);
            case GOLD:
                return new Image(goldMedalImg);
            case DEV:
                return new Image(devMedalImg);
            default:
                return new Image();
        }
    }
}
