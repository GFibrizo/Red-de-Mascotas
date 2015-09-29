/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.support.android.designlibdemo;

import java.util.ArrayList;
import java.util.Random;

public class Constants {

    private static final Random RANDOM = new Random();

    public static int getRandomCheeseDrawable() {
        switch (RANDOM.nextInt(5)) {
            default:
            case 0:
                return R.drawable.orange_kitten;
            case 1:
                return R.drawable.grey_cat;
            case 2:
                return R.drawable.pardo_cat;
            case 3:
                return R.drawable.grey_white_kitten;
            case 4:
                return R.drawable.black_cat;
        }
    }

    public static final String[] sCheeseStrings = {
            "Tito", "Simba", "Fiona", "Kiara", "Lucky", "Melba", "Nala", "Pumba", "Rubi",
            "Canela", "Ambar", "Cleopatra", "Casandra", "Luna", "Fuego"
    };


    public static final String[] dogs = {"Affenpinscher", "Afgano", "Airedale Terrier", "Akita", "Alaskan Malamute",
    "American Foxhound", "American Staffordshire", "Terrier Antiguo perro pastor inglés", "Basenji", "Basset Hound",
    "Beagle", "Beauceron", "Bedlington Terrier", "Bichon Frise", "Bichón habanero", "Bichón maltés", "Bloodhound",
    "Perro de San Huberto", "Bluetick Coonhound", "Border Collie", "Border Terrier", "Borzoi", "Boston Terrier",
    "Boxer", "Boyero de Berna", "Boyero de Entlebuch", "Boyero de Flandes", "Boykin Spaniel", "Braco alemán de pelo corto",
    "Kurzhaar", "Braco alemán de pelo duro", "Drahthaar", "Braco de Weimar", "Weimaraner", "Buhund noruego", "Bull Terrier",
    "Bull Terrier Miniatura", "Bulldog", "Bulldog francés", "Bullmastiff", "Cairn Terrier", "Cane Corso", "Caniche",
    "Cavalier King Charles Spaniel", "Cazador de alces noruego", "Chihuahua", "Chinook", "Chow Chow", "Clumber Spaniel",
    "Cocker Spaniel", "Cocker spaniel inglés", "Collie", "Collie barbudo", "Coonhound Inglés Americano",
    "Coonhound negro y bronce", "Corgi galés de Cardigan", "Corgi galés de Pembroke", "Crestado Chino", "Dachshund",
    "Dandie Dinmont Terrier", "Doberman", "Dogo de Burdeos", "Dogo del Tíbet", "Dálmata", "Finnish Lapphund",
    "Finnish Spitz", "Fox terrier de pelo duro", "Foxhound inglés", "Galgo inglés", "Glen of Imaal Terrier",
    "Golden Retriever", "Gordon Setter", "Gran boyero suizo", "Gran danés", "Grifón de Bruselas", "Grifón Korthal",
    "Grifón vandeano basset pequeño", "Harrier", "Husky siberiano", "Jack Russell terrier", "Keeshond", "Kerry Blue Terrier",
    "Komondor", "Kuvasz", "Labrador Retriever", "Lakeland Terrier", "Lebrel escocés", "Lebrel italiano", "Leonberger",
    "Lhasa Apso", "Lobero irlandés", "Lundehund", "Manchester Terrier", "Mastín inglés", "Mastín napolitano",
    "Mestizo", "Montaña de los Pirineos", "Gran Pirineo", "Otterhound", "Papillon", "Parson Russell Terrier",
    "Pastor alemán",  "Overjero Alemán", "Pastor belga", "Pastor belga Malinois", "Pastor belga Tervuerense",
    "Pastor de Anatolia", "Pastor de Brie", "Pastor de los Pirineos", "Pastor de Valée", "Pastor ganadero autraliano",
    "Pastor Islandés", "Pastor ovejero australiano", "Pekinés", "Pequeño perro león", "Perro crestado rodesiano",
    "Perro de agua americano", "Perro de agua irlandés", "Perro de Agua Portugués", "Perro de Canaán",
    "Perro esquimal americano", "Perro pastor de las islas Shetland", "Pharaoh Hound", "Pinscher alemán",
    "Pinscher miniatura", "Plott", "Podenco ibicenco", "Podenco portugués", "Pointer", "Pomerania", "Pug", "Puli",
    "Redbone Coonhound", "Retriever de Chesapeake", "Retriever de Nueva Escocia", "Retriever de Pelo Liso",
    "Retriever de pelo rizado", "Rottweiler", "Saluki", "Samoyedo", "San Bernardo", "Schipperke", "Schnauzer estándar",
    "Schnauzer gigante", "Schnauzer miniatura", "Sealyham Terrier", "Setter inglés", "Setter irlandés",
    "Setter irlandés rojo y blanco", "Shar Pei", "Shiba Inu", "Shih Tzu", "Skye Terrier", "Soft Coated Wheaten Terrier",
    "Spaniel bretón", "Spaniel de campo", "Spaniel japonés", "Spaniel tibetano", "Spinone", "Springer spaniel galés",
    "Springer spaniel inglés", "Staffordshire Bull Terrier", "Sussex Spaniel", "Terranova", "Terrier australiano",
    "Terrier checo", "Terrier de Australia", "Terrier de Norfolk", "Terrier de Norwich", "Terrier escocés",
    "Terrier galés", "Terrier irlandés", "Terrier ruso negro", "Terrier tibetano", "Toy Fox Terrier",
    "Toy spaniel inglés", "Treeing Walker Coonhound", "Vallhund sueco", "Vizsla", "West Highland White Terrier",
    "Whippet", "Xoloitzcuintli", "Yorkshire Terrier"
    };

    public static final String[] cats = {"Abisinio", "Aphrodite's Giants", "Australian Mist", "American Curl", "Azul ruso",
    "American shorthair", "American wirehair", "Angora turco", "Africano doméstico", "Bengala", "Bobtail japonés",
    "Bombay", "Bosque de Noruega", "Brazilian Shorthair", "Brivon de pelo corto", "Brivon de pelo largo",
    "British Shorthair", "Burmés", "Burmilla", "Cornish rexx", "California Spangled", "Ceylon", "Cymric",
    "Chartreux", "Deutsch Langhaar", "Devon rex", "Dorado africano", "Don Sphynx", "Dragon Li", "Europeo Común",
    "Exótico de Pelo Corto", "FoldEx", "German Rex", "Habana brown", "Himalayo", "Korat", "Khao Manee", "Lituli",
    "Maine Coon", "Manx", "Mau egipcio", "Munchkin", "Ocicat", "Oriental", "Oriental de pelo largo", "Ojos azules",
    "PerFold1", "Persa Americano o Moderno", "Persa Clásico o Tradicional", "Peterbald", "Pixie Bob", "Ragdoll",
    "Sagrado de Birmania", "Scottish Fold", "Selkirk rex", "Serengeti", "Seychellois", "Siamés", "Siamés Moderno",
    "Siamés Tradicional", "Siberiano", "Snowshoe", "Sphynx", "Tonkinés", "Van Turco"
    };

    public static String[] ages = {"0 - 6 meses", "6 - 12 meses", "1 - 3 años", "3 - 7 años", "mas de 7 años"};
    public static String[] sizes = {"Muy chico", "Chico", "Mediano", "Grande"};

    public static String[] hairColors = {"Negro", "Blanco", "Marron", "Gris", "Verde"};
    public static String[] eyesColors = {"Negros", "Marrones", "Azules", "Verdes", "Blancos", "Celestes"};
}
