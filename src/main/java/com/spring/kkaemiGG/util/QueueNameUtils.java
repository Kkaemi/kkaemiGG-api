package com.spring.kkaemiGG.util;

import no.stelar7.api.r4j.basic.constants.types.lol.GameQueueType;

public class QueueNameUtils {

    public static String toPrettyName(GameQueueType gameQueueType) {
        return prettyName(gameQueueType);
    }

    private static String prettyName(GameQueueType gameQueueType) {
        switch (gameQueueType) {
            case CUSTOM:
                return "커스텀";
            case NORMAL_3X3_BLIND_PICK_OLD:
            case NORMAL_5X5_BLIND_PICK_OLD:
            case NORMAL_5V5_BLIND_PICK:
                return "일반";
            case NORMAL_5X5_DRAFT:
                return "5v5 Draft Pick";
            case RANKED_PREMADE_5X5:
                return "5v5 Ranked Duo";
            case RANKED_PREMADE_3X3:
                return "3v3 Ranked Duo";
            case RANKED_TEAM_3X3:
                return "3v3 Ranked Team";
            case RANKED_TEAM_5X5:
                return "5v5 Ranked Team";
            case ODIN_5X5_BLIND:
                return "Dominion Blind Pick";
            case ODIN_5X5_DRAFT:
                return "Dominion Draft Pick";
            case BOT_5X5:
                return "Summoners Rift Bots";
            case BOT_ODIN_5X5:
                return "Dominion Bots";
            case BOT_5X5_INTRO_OLD:
                return "Summoners Rift Bots Intro Difficulty";
            case BOT_5X5_BEGINNER_OLD:
                return "Summoners Rift Bots Beginner Difficulty";
            case BOT_5X5_INTERMEDIATE_OLD:
                return "Summoners Rift Bots Intermediate Difficulty";
            case BOT_3X3_BEGINNER_OLD:
                return "Twisted Treeline Bots Beginner Difficulty";
            case GROUP_FINDER_5X5:
                return "Teambuilder";
            case ONEFORALL_5X5:
                return "One for All";
            case FIRSTBLOOD_1X1:
                return "1v1 Snowdown Showdown";
            case FIRSTBLOOD_2X2:
                return "2v2 Snowdown Showdown";
            case HEXAKILL_6X6_SR:
                return "Summoners Rift Hexakill";
            case URF_5X5:
                return "URF";
            case ONE_FOR_ALL_MIRROR:
                return "One for All Mirror Mode";
            case BOT_URF_5X5:
                return "URF Bots";
            case NIGHTMARE_BOT_5X5_RANK1:
                return "Doombots Rank 1";
            case NIGHTMARE_BOT_5X5_RANK2:
                return "Doombots Rank 2";
            case NIGHTMARE_BOT_5X5_RANK5:
                return "Doombots Rank 5";
            case ASCENSION_5X5:
                return "Ascension";
            case HEXAKILL:
                return "Hexakill";
            case BILGEWATER_ARAM_5X5:
                return "ARAM Butchers Bridge Version";
            case KING_PORO_5X5:
                return "King Poro";
            case COUNTER_PICK:
                return "Nemesis";
            case BILGEWATER_5X5:
                return "Blackmarket Brawlers";
            case NEXUS_SIEGE_OLD:
                return "Nexus Siege";
            case DEFINITELY_NOT_DOMINION_5X5:
                return "Definitely Not Dominion";
            case ALL_RANDOM_URF:
                return "ARURF";
            case SNOW_BATTLE_ARURF:
                return "ARURF Snow Skin";
            case OVERCHARGE:
                return "Overcharge";
            case TEAM_BUILDER_DRAFT_UNRANKED_5X5:
                return "5v5 Dynamic Queue";
            case TEAM_BUILDER_DRAFT_RANKED_5X5:
                return "5v5 Dynamic Ranked Queue";
            case TEAM_BUILDER_RANKED_SOLO:
            case RANKED_SOLO_5X5:
                return "솔로 랭크";
            case RANKED_FLEX_SR:
                return "자유 랭크";
            case ARAM:
            case ARAM_5X5:
            case ARAM_5X5_OLD:
                return "칼바람 나락";
            case NORMAL_3X3_BLIND_PICK:
                return "3v3 Normal Flex Queue";
            case RANKED_FLEX_TT:
                return "3v3 Ranked Flex Queue";
            case ASSASSINATE_5X5:
                return "Blood Hunt Assassin";
            case DARKSTAR_3X3:
                return "Darkstar";
            case CLASH:
                return "Clash";
            case BOT_3X3_INTERMEDIATE:
            case BOT_3X3_INTRO:
            case BOT_3X3_BEGINNER:
            case BOT_5X5_INTRO:
            case BOT_5X5_BEGINNER:
            case BOT_5X5_INTERMEDIATE:
                return "봇전";
            case NEXUS_SIEGE:
                return "Nexus Siege";
            case NIGHTMARE_BOT_5X5_VOTE:
                return "Doombots Vote Difficulty";
            case NIGHTMARE_BOT_5X5:
                return "Doombots";
            case INVASION_NORMAL:
                return "Invasion Normal Difficulty";
            case INVASION_ONSLAUGHT:
                return "Invasion Onslaught Difficulty";
            case NEXUS_BLITZ:
                return "Nexus Blitz";
            case ODYSSEY_INTRO:
                return "Odyssey Intro Difficulty";
            case ODYSSEY_CADET:
                return "Odyssey Cadet Difficulty";
            case ODYSSEY_CREWMEMBER:
                return "Odyssey Crewmember Difficulty";
            case ODYSSEY_CAPTAIN:
                return "Odyssey Captain Difficulty";
            case ODYSSEY_ONSLAUGHT:
                return "Odyssey Onslaught Difficulty";
            case TUTORIAL_MODULE_1:
            case TUTORIAL_MODULE_2:
            case TUTORIAL_MODULE_3:
                return "튜토리얼";
            case TEAMFIGHT_TACTICS:
                return "Teamfight Tactics";
            case TEAMFIGHT_TACTICS_RANKED:
                return "Teamfight Tactics Ranked";
            case TEAMFIGHT_TACTICS_TUTORIAL:
                return "Teamfight Tactics Tutorial";
            case TEAMFIGHT_TACTICS_HYPER_ROLL:
                return "Teamfight Tactics Hyper Roll";
            case ULTBOOK:
                return "궁극기 주문서";
            default:
                return "This enum does not have a pretty name";
        }
    }
}
