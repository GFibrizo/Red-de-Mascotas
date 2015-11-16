package model;

import java.util.List;

import static utils.Constants.*;

public class MatchingPet implements Comparable<MatchingPet> {

    public String id;

    public String type;

    public String contactEmail;

    public String breed;

    public String gender;

    public String size;

    public List<String> colors;

    public String eyeColor;

    public List<String> images;

    public GeoLocation lastSeenOrFoundLocation;

    public String lastSeenOrFoundDate;

    public String publicationType;

    public int matchingScore;


    public MatchingPet(FoundPet foundPetMatch, LostPet lostPet, String contactEmail) {
        this.id = foundPetMatch.id;
        this.type = foundPetMatch.type;
        this.contactEmail = contactEmail;
        this.breed = foundPetMatch.breed;
        this.gender = foundPetMatch.gender;
        this.size = foundPetMatch.size;
        this.colors = foundPetMatch.colors;
        this.eyeColor = foundPetMatch.eyeColor;
        this.images = foundPetMatch.images;
        this.lastSeenOrFoundLocation = foundPetMatch.foundLocation;
        this.lastSeenOrFoundDate = foundPetMatch.foundDate;
        this.publicationType = FOUND;
        this.matchingScore = getMatchingScore(lostPet.breed, lostPet.size, lostPet.colors, lostPet.eyeColor);
    }

    public MatchingPet(LostPet lostPetMatch, FoundPet foundPet, String contactEmail) {
        this.id = lostPetMatch.id;
        this.type = lostPetMatch.type;
        this.contactEmail = contactEmail;
        this.breed = lostPetMatch.breed;
        this.gender = lostPetMatch.gender;
        this.size = lostPetMatch.size;
        this.colors = lostPetMatch.colors;
        this.eyeColor = lostPetMatch.eyeColor;
        this.images = lostPetMatch.images;
        this.lastSeenOrFoundLocation = lostPetMatch.lastSeenLocation;
        this.lastSeenOrFoundDate = lostPetMatch.lastSeenDate;
        this.publicationType = LOST;
        this.matchingScore = getMatchingScore(foundPet.breed, foundPet.size, foundPet.colors, foundPet.eyeColor);
    }

    private int getMatchingScore(String breed, String size, List<String> colors, String eyeColor) {
        int score = 0;
        if (this.breed != null && breed != null && this.breed.compareTo(breed) == 0)
            score += BREED_SCORE;
        if (this.size != null && size != null && this.size.compareTo(size) == 0)
            score += SIZE_SCORE;
        if (this.colors.size() > 0 && colors.size() > 0 && this.colors.get(0).compareTo(colors.get(0)) == 0)
            score += MAIN_COLOR_SCORE;
        if (this.eyeColor != null && eyeColor != null && this.eyeColor.compareTo(eyeColor) == 0)
            score += EYE_COLOR_SCORE;
        if (this.colors.size() > 1 && colors.size() > 1 && this.colors.get(1).compareTo(colors.get(1)) == 0)
            score += SECONDARY_COLOR_SCORE;
        return score;
    }

    @Override
    public int compareTo(MatchingPet another) {
        if (this.matchingScore < another.matchingScore) {
            return -1;
        } else {
            return 1;
        }
    }

}
