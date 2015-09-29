package model.external;

import utils.Decoder;

import java.util.List;

public class SearchForAdoptionFilters {

    public String type;

    public String breed;

    public List<String> genders;

    public List<String> ages;

    public List<String> sizes;

    public List<String> colors;

    public List<String> eyeColors;

    public String neighbourhood;

    public String city;

    public void decodeFilters() {
        this.breed = Decoder.decode(this.breed);
        this.ages = Decoder.decode(this.ages);
        this.colors = Decoder.decode(this.colors);
        this.eyeColors = Decoder.decode(this.eyeColors);
        this.neighbourhood = Decoder.decode(this.neighbourhood);
        this.city = Decoder.decode(this.city);
    }

}
