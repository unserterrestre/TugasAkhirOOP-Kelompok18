package com.ProyekPermainanOOP.blockblast.model;

import com.ProyekPermainanOOP.blockblast.localization.LocalizationManager.Language;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PusakaCompendium {

    public static class LocalizedLore {
        private final String title;
        private final String[] lines;

        public LocalizedLore(String title, String[] lines) {
            this.title = title;
            this.lines = lines;
        }

        public String getTitle() {
            return title;
        }

        public String[] getLines() {
            return lines;
        }
    }

    public static class RelicEntry {
        private final int baseTileId;
        private final String pinyin;
        private final Map<Language, LocalizedLore> loreByLanguage;
        private int clearedCount;

        public RelicEntry(int baseTileId, String pinyin) {
            this.baseTileId = baseTileId;
            this.pinyin = pinyin;
            this.loreByLanguage = new EnumMap<>(Language.class);
            this.clearedCount = 0;
        }

        public RelicEntry addLore(Language lang, String title, String... lines) {
            loreByLanguage.put(lang, new LocalizedLore(title, lines));
            return this;
        }

        public int getBaseTileId() {
            return baseTileId;
        }

        public String getPinyin() {
            return pinyin;
        }

        public LocalizedLore getLore(Language lang) {
            LocalizedLore lore = loreByLanguage.get(lang);
            if (lore != null) {
                return lore;
            }
            return loreByLanguage.get(Language.ENGLISH);
        }

        public int getClearedCount() {
            return clearedCount;
        }

        public boolean isUnlocked() {
            return clearedCount >= 4;
        }

        public void addCleared(int count) {
            this.clearedCount += count;
        }
    }

    private static PusakaCompendium instance;
    private final List<RelicEntry> entries;
    private int currentPageIndex;

    private PusakaCompendium() {
        this.currentPageIndex = 0;
        this.entries = new ArrayList<>();

        entries.add(new RelicEntry(1, "JING")
                .addLore(Language.INDONESIA, "Ibu Kota & Kejayaan", "Melambangkan pusat peradaban dan", "tata kota kuno yang kokoh.", "Pencapaian besar bermula dari", "fondasi dan arah yang jelas.")
                .addLore(Language.ENGLISH, "Capital & Glory", "Symbolizes the heart of civilization", "and grand ancient architecture.", "Every great achievement begins", "with a firm and clear foundation.")
                .addLore(Language.MELAYU, "Ibu Kota & Kejayaan", "Melambangkan pusat tamadun dan", "seni bina kota purba yang teguh.", "Kejayaan besar bermula daripada", "asas dan hala tuju yang jelas.")
                .addLore(Language.JAWA, "Kutha Krajan & Kamulyan", "Nggambarake punjering peradaban", "lan tatanan kutha kuna kang kukuh.", "Kasil kang gedhe diwiwiti saka", "dhasar lan tujuan kang cetha.")
                .addLore(Language.SUNDA, "Puseur Dayeuh & Kajayaan", "Ngalambangkeun puseur peradaban", "jeung tatanan kota kuno nu kuat.", "Kasuksesan gede dimimitian tina", "fondasi jeung arah anu jelas.")
                .addLore(Language.ESPANOL, "Capital y Gloria", "Simboliza el centro de la civilizacion", "y la grandeza arquitectonica antigua.", "Todo gran logro comienza con", "una base solida y un rumbo claro.")
                .addLore(Language.FRANCAIS, "Capitale et Gloire", "Symbolise le coeur de la civilisation", "et la grandeur des cites anciennes.", "Toute grande reussite commence", "par une base solide et claire.")
                .addLore(Language.DEUTSCH, "Hauptstadt & Ruhm", "Symbolisiert das Zentrum der Kultur", "und antike Baukunst von Bestand.", "Jeder grosse Erfolg beginnt mit", "einem festen, klaren Fundament.")
                .addLore(Language.ITALIANO, "Capitale e Gloria", "Simboleggia il cuore della civilta", "e la grandezza delle citta antiche.", "Ogni grande traguardo nasce da", "basi solide e direzione chiara.")
                .addLore(Language.PORTUGUES, "Capital e Gloria", "Simboliza o centro da civilizacao", "e a grandeza das cidades antigas.", "Toda grande conquista comeca com", "uma base firme e direcao clara.")
                .addLore(Language.NEDERLANDS, "Hoofdstad & Glorie", "Symboliseert het hart van beschaving", "en grootse eeuwenoude bouwkunst.", "Elke grote prestatie begint met", "een stevig en helder fundament.")
                .addLore(Language.POLSKI, "Stolica i Chwala", "Symbolizuje centrum cywilizacji", "oraz potege dawnych grodow.", "Kazdy wielki sukces zaczyna sie", "od mocnych i jasnych fundamentow.")
                .addLore(Language.TURKCE, "Baskent ve Zafer", "Medeniyetin kalbini ve kadim", "mimari gorkemi simgeler.", "Her buyuk basari saglam bir", "temel ve net bir yonle baslar.")
                .addLore(Language.SVENSKA, "Huvudstad & Ara", "Symboliserar civilisationens hjarta", "och forntida arkitektonisk prakt.", "Varje stor framgang borjar med", "en stadig och tydlig grund.")
                .addLore(Language.NORSK, "Hovedstad & Aere", "Symboliserer sivilisasjonens hjerte", "og eldgammel storslagen byggeskikk.", "Enhver stor bragd begynner med", "et solid og tydelig fundament.")
                .addLore(Language.DANSK, "Hovedstad & Aere", "Symboliserer civilisationens hjerte", "og oldtidens storslaaede bykunst.", "Enhver stor bedrift begynder med", "et fast og tydeligt fundament.")
                .addLore(Language.SUOMI, "Paakaupunki & Kunnia", "Symboloi sivistyksen sydanta", "ja muinaista mahtavaa rakennustaitoa.", "Jokainen suuri saavutus alkaa", "vahvalta ja selkealta perustalta.")
                .addLore(Language.TAGALOG, "Kabisera at Kaluwalhatian", "Sumasagisag sa puso ng kabihasnan", "at matibay na sinaunang lungsod.", "Ang bawat dakilang tagumpay ay", "nagsisimula sa matatag na pundasyon.")
                .addLore(Language.VIETNAMESE, "Kinh Do & Vinh Quang", "Tuong trung cho trung tam van minh", "va kien truc co dai vung chac.", "Moi thanh tuu lon deu bat dau", "tu nen tang va dinh huong ro rang.")
                .addLore(Language.SWAHILI, "Mji Mkuu na Utukufu", "Inaashiria kitovu cha ustaarabu", "na ujenzi imara wa kale.", "Kila mafanikio makubwa huanza", "na msingi imara na mwelekeo."));

        entries.add(new RelicEntry(2, "YUN")
                .addLore(Language.INDONESIA, "Awan & Ketenteraman", "Menggambarkan awan tinggi yang", "bergerak bebas tanpa paksaan.", "Simbol keluwesan pikiran dan", "ketenangan batin di tengah ujian.")
                .addLore(Language.ENGLISH, "Cloud & Serenity", "Depicts high clouds drifting freely", "with the wind without resistance.", "Stands for mental flexibility and", "inner calm under any pressure.")
                .addLore(Language.MELAYU, "Awan & Ketenteraman", "Menggambarkan awan tinggi yang", "bergerak bebas mengikut angin.", "Simbol kelenturan fikiran dan", "ketenangan jiwa dalam ujian.")
                .addLore(Language.JAWA, "Mega & Katentreman", "Nggambarake mega dhuwur kang", "lumaku bebas manut lakuning angin.", "Pratandha pikiran kang luwes lan", "ati kang tentrem ing kahanan.")
                .addLore(Language.SUNDA, "Mega & Katengtreman", "Ngagambarkeun mega luhur anu", "ngalayang bebas nuturkeun angin.", "Lambang pikiran anu luwes jeung", "katenangan batin dina ujian.")
                .addLore(Language.ESPANOL, "Nube y Serenidad", "Muestra nubes altas que fluyen", "libremente con el viento.", "Representa flexibilidad mental", "y calma interior ante la presion.")
                .addLore(Language.FRANCAIS, "Nuage et Serenite", "Depeint de hauts nuages flottant", "librement au gre du vent.", "Symbole de souplesse d'esprit", "et de calme interieur.")
                .addLore(Language.DEUTSCH, "Wolke & Gelassenheit", "Zeigt hohe Wolken, die frei", "ohne Widerstand im Wind ziehen.", "Steht fuer geistige Flexibilitaet", "und innere Ruhe unter Druck.")
                .addLore(Language.ITALIANO, "Nuvola e Serenita", "Raffigura alte nuvole che fluttuano", "libere nel vento senza sforzo.", "Simbolo di flessibilita mentale", "e calma interiore nelle sfide.")
                .addLore(Language.PORTUGUES, "Nuvem e Serenidade", "Retrata nuvens altas que flutuam", "livremente ao sabor do vento.", "Simbolo de flexibilidade mental", "e calma interior sob pressao.")
                .addLore(Language.NEDERLANDS, "Wolk & Sereniteit", "Toont hoge wolken die vrij en", "moeiteloos met de wind meedrijven.", "Staat voor geestelijke souplesse", "en innerlijke kalmte onder druk.")
                .addLore(Language.POLSKI, "Chmura i Spokoj", "Przedstawia wysokie chmury", "plynace swobodnie z wiatrem.", "Symbolizuje elastycznosc umyslu", "i wewnetrzny spokoj w trudach.")
                .addLore(Language.TURKCE, "Bulut ve Huzur", "Ruzgarla ozgurce suzulen", "yuksek bulutlari tasvir eder.", "Zihinsel esnekligi ve baski", "altinda ic huzuru simgeler.")
                .addLore(Language.SVENSKA, "Moln & Stillhet", "Skildrar hoga moln som svavar", "fritt med vinden utan motstand.", "Star for mental smidighet och", "inre lugn under alla provningar.")
                .addLore(Language.NORSK, "Sky & Sinnsro", "Viser hoye skyer som driver", "fritt med vinden uten motstand.", "Star for mental smidighet og", "indre ro under press.")
                .addLore(Language.DANSK, "Sky & Sindsro", "Skildrer hoje skyer der driver", "frit med vinden uden modstand.", "Star for mental smidighed og", "indre ro i enhver udfordring.")
                .addLore(Language.SUOMI, "Pilvi & Tyyneys", "Kuvaa korkeita pilvia, jotka", "lipuvat vapaasti tuulen mukana.", "Symboloi mielen joustavuutta", "ja sisaisen rauhan sailyttamista.")
                .addLore(Language.TAGALOG, "Ulap at Kapayapaan", "Naglalarawan ng matataas na ulap", "na malayang sumasabay sa hangin.", "Sagisag ng malawak na isipan at", "panloob na kapanatagan.")
                .addLore(Language.VIETNAMESE, "May & Thanh Than", "Mieu ta nhung tang may cao", "troi tu do theo lan gio.", "Bieu tuong cua su linh hoat", "va tam hon binh an truoc thu thach.")
                .addLore(Language.SWAHILI, "Wingu na Utulivu", "Inaonyesha mawingu ya juu", "yanayoelea huru na upepo.", "Ishara ya unyumbufu wa akili", "na utulivu wa ndani wakati mgumu."));

        entries.add(new RelicEntry(3, "SHI")
                .addLore(Language.INDONESIA, "Karya & Pengabdian", "Bermakna tindakan nyata dan", "tanggung jawab menuntaskan tugas.", "Kebijaksanaan sejati dibuktikan", "melalui karya yang bermakna.")
                .addLore(Language.ENGLISH, "Deed & Devotion", "Signifies purposeful action and", "dedication in fulfilling duties.", "True wisdom is proven not in", "thought alone, but through deeds.")
                .addLore(Language.MELAYU, "Karya & Pengabdian", "Bermakna tindakan nyata dan", "tanggungjawab menyempurnakan tugas.", "Kebijaksanaan sejati dibuktikan", "melalui hasil kerja yang bermakna.")
                .addLore(Language.JAWA, "Karya & Pengabdian", "Tegese tumindak nyata lan", "tanggung jawab ngrampungake tugas.", "Kawicaksanan sejati kabuktekake", "liwat karya kang migunani.")
                .addLore(Language.SUNDA, "Karya & Pangabdian", "Hartina tindakan nyata jeung", "tanggung jawab ngarengsekeun tugas.", "Kawijaksanaan sajati dibuktikeun", "ku karya anu aya mangpaatna.")
                .addLore(Language.ESPANOL, "Obra y Devocion", "Significa accion con proposito y", "dedicacion al cumplir el deber.", "La verdadera sabiduria se prueba", "con hechos significativos.")
                .addLore(Language.FRANCAIS, "Oeuvre et Devouement", "Signifie l'action concrete et le", "devouement dans chaque tache.", "La vraie sagesse se prouve", "par des actes significatifs.")
                .addLore(Language.DEUTSCH, "Tat & Hingabe", "Bedeutet zielgerichtetes Handeln", "und Pflichtbewusstsein im Werk.", "Wahre Weisheit zeigt sich nicht", "nur im Denken, sondern in Taten.")
                .addLore(Language.ITALIANO, "Opera e Devozione", "Significa azione concreta e", "dedizione nel compiere il dovere.", "La vera saggezza si dimostra", "attraverso opere significative.")
                .addLore(Language.PORTUGUES, "Obra e Devocao", "Significa acao com proposito e", "dedicacao ao cumprir tarefas.", "A verdadeira sabedoria prova-se", "por meio de obras relevantes.")
                .addLore(Language.NEDERLANDS, "Daad & Toewijding", "Betekent doelgericht handelen en", "toewijding bij elke taak.", "Ware wijsheid bewijst zich", "door betekenisvolle daden.")
                .addLore(Language.POLSKI, "Dzielo i Oddanie", "Oznacza celowe dzialanie oraz", "oddanie w wypelnianiu obowiazkow.", "Prawdziwa madrosc potwierdza", "sie poprzez wartosciowe czyny.")
                .addLore(Language.TURKCE, "Eser ve Adanmislik", "Amacli eylemi ve gorevleri", "tamamlama sorumlulugunu anlatir.", "Gercek bilgelik yalnizca dusunceyle", "degil anlamli eserlerle kanitlanir.")
                .addLore(Language.SVENSKA, "Garning & Hangivenhet", "Betyder malmedveten handling och", "hangivenhet i varje uppgift.", "Sann visdom bevisas genom", "meningsfulla garningar.")
                .addLore(Language.NORSK, "Gjerning & Hengivenhet", "Betyr malrettet handling og", "hengivenhet i a fullfore plikter.", "Sann visdom bevises gjennom", "meningsfulle gjerninger.")
                .addLore(Language.DANSK, "Gerning & Hengivenhed", "Betyder malrettet handling og", "hengivenhed i at loese opgaver.", "Sand visdom bevises gennem", "meningsfulde gerninger.")
                .addLore(Language.SUOMI, "Teko & Omistautuminen", "Tarkoittaa maaratietoista tekoa", "ja omistautumista tehtaville.", "Todellinen viisaus osoitetaan", "merkityksellisilla teoilla.")
                .addLore(Language.TAGALOG, "Gawa at Debosyon", "Nangangahulugan ng makabuluhang", "kilos at katapatan sa tungkulin.", "Ang tunay na karunungan ay", "pinatutunayan sa mabuting gawa.")
                .addLore(Language.VIETNAMESE, "Su Nghiep & Tan Tam", "Bieu thi hanh dong thiet thuc", "va trach nhiem hoan thanh nhiem vu.", "Tri tue chan chinh duoc chung minh", "qua nhung viec lam y nghia.")
                .addLore(Language.SWAHILI, "Matendo na Kujitolea", "Inamaanisha vitendo vya kusudi", "na kujitolea kutimiza wajibu.", "Hekima ya kweli hudhihirishwa", "kupitia matendo yenye maana."));

        entries.add(new RelicEntry(4, "MAI")
                .addLore(Language.INDONESIA, "Niaga & Kemakmuran", "Mewakili pertukaran nilai dan", "kecerdikan membaca peluang.", "Niaga yang jujur menumbuhkan", "kesejahteraan bersama.")
                .addLore(Language.ENGLISH, "Trade & Prosperity", "Represents fair exchange and", "keen foresight for opportunity.", "Honest commerce bridges lands", "and sustains shared prosperity.")
                .addLore(Language.MELAYU, "Niaga & Kemakmuran", "Mewakili pertukaran nilai dan", "kebijaksanaan membaca peluang.", "Perniagaan jujur memupuk", "kemakmuran bersama.")
                .addLore(Language.JAWA, "Niaga & Kamakmuran", "Nggambarake ijol-ijolan nilai lan", "kawasisan maca kalodhangan.", "Niaga kang jujur nuwuhake", "karaharjan kanggo kabeh.")
                .addLore(Language.SUNDA, "Niaga & Kamakmuran", "Ngawakilan silih tukeur nilai", "jeung kapinteran maca kasempetan.", "Niaga anu jujur numuwuhkeun", "karaharjaan sarerea.")
                .addLore(Language.ESPANOL, "Comercio y Prosperidad", "Representa el intercambio justo y", "la vision para detectar oportunidades.", "El comercio honesto une regiones", "y fomenta la prosperidad comun.")
                .addLore(Language.FRANCAIS, "Commerce et Prosperite", "Represente l'echange equitable et", "l'art de saisir les opportunites.", "Le commerce honnete favorise", "une prosperite durable.")
                .addLore(Language.DEUTSCH, "Handel & Wohlstand", "Steht fuer fairen Austausch und", "Weitblick fuer gute Gelegenheiten.", "Ehrlicher Handel verbindet", "und schafft gemeinsamen Wohlstand.")
                .addLore(Language.ITALIANO, "Commercio e Prosperita", "Rappresenta lo scambio equo e", "la lungimiranza nelle opportunita.", "Il commercio onesto unisce i popoli", "e genera prosperita condivisa.")
                .addLore(Language.PORTUGUES, "Comercio e Prosperidade", "Representa a troca justa e a", "visao estrategica de oportunidades.", "O comercio honesto conecta povos", "e gera prosperidade partilhada.")
                .addLore(Language.NEDERLANDS, "Handel & Welvaart", "Vertegenwoordigt eerlijke ruil en", "scherp inzicht in nieuwe kansen.", "Eerlijke handel verbindt regios", "en brengt gedeelde welvaart.")
                .addLore(Language.POLSKI, "Handel i Dobrobyt", "Symbolizuje uczciwa wymiane", "oraz trafne dostrzeganie szans.", "Rzetelny handel laczy ludzi", "i buduje wspolny dobrobyt.")
                .addLore(Language.TURKCE, "Ticaret ve Refah", "Adil degisimi ve firsatlari", "ongorme becerisini temsil eder.", "Durust ticaret bolgeleri birlestirir", "ve ortak refahi buyutur.")
                .addLore(Language.SVENSKA, "Handel & Valstand", "Representerar rattvist utbyte och", "blick for nya mojligheter.", "Arlig handel bygger broar och", "skapar gemensamt valstand.")
                .addLore(Language.NORSK, "Handel & Velstand", "Representerer rettferdig bytte", "og blikk for gode muligheter.", "Aerlig handel knytter folk sammen", "og skaper felles velstand.")
                .addLore(Language.DANSK, "Handel & Velstand", "Repraesenterer fair udveksling", "og blik for gode muligheder.", "Aerlig handel bygger bro og", "skaber faelles velstand.")
                .addLore(Language.SUOMI, "Kauppa & Vauraus", "Edustaa reilua vaihdantaa ja", "kykya nahda uusia mahdollisuuksia.", "Rehellinen kauppa yhdistaa", "ja luo yhteista vaurautta.")
                .addLore(Language.TAGALOG, "Kalakalan at Kasaganaan", "Kumakatawan sa patas na palitan", "at talino sa pagbasa ng pagkakataon.", "Ang tapat na kalakalan ay", "nagbubunga ng kasaganaan.")
                .addLore(Language.VIETNAMESE, "Thuong Nghiep & Thinh Vuong", "Dai dien cho su trao doi cong bang", "va tam nhin nam bat co hoi.", "Giao thuong chan chinh ket noi", "va tao dung thinh vuong chung.")
                .addLore(Language.SWAHILI, "Biashara na Ustawi", "Inawakilisha ubadilishanaji haki", "na uwezo wa kuona fursa.", "Biashara ya uaminifu huleta", "ustawi wa pamoja."));

        entries.add(new RelicEntry(5, "SHU")
                .addLore(Language.INDONESIA, "Kitab & Ilmu Pengetahuan", "Melambangkan gulungan naskah", "kuno dan cahaya literasi.", "Ilmu pengetahuan adalah pusaka", "abadi lintas generasi.")
                .addLore(Language.ENGLISH, "Scroll & Knowledge", "Symbolizes ancient bamboo scrolls", "and the light of literacy.", "Knowledge is an enduring heirloom", "passed across generations.")
                .addLore(Language.MELAYU, "Kitab & Ilmu Pengetahuan", "Melambangkan gulungan naskah", "purba dan cahaya persuratan.", "Ilmu pengetahuan ialah pusaka", "abadi merentas generasi.")
                .addLore(Language.JAWA, "Kitab & Kawruh", "Nglambangake gulungan naskah", "kuna lan pepadhanging sastra.", "Kawruh iku pusaka langgeng", "kang diwarisake turun-temurun.")
                .addLore(Language.SUNDA, "Kitab & Elmu Pangaweruh", "Ngalambangkeun gulungan naskah", "kuno jeung cahaya literasi.", "Elmu pangaweruh nyaeta pusaka", "abadi lintas generasi.")
                .addLore(Language.ESPANOL, "Libro y Conocimiento", "Simboliza pergaminos antiguos", "y la luz del aprendizaje.", "El conocimiento es un legado", "eterno entre generaciones.")
                .addLore(Language.FRANCAIS, "Livre et Savoir", "Symbolise les parchemins anciens", "et la lumiere de l'erudition.", "Le savoir est un heritage", "eternel transmis aux generations.")
                .addLore(Language.DEUTSCH, "Buch & Wissen", "Symbolisiert antike Schriftrollen", "und das Licht der Bildung.", "Wissen ist ein unvergängliches", "Erbe ueber Generationen hinweg.")
                .addLore(Language.ITALIANO, "Libro e Conoscenza", "Simboleggia antichi rotoli scritti", "e la luce del sapere.", "La conoscenza e un'eredita", "eterna tra le generazioni.")
                .addLore(Language.PORTUGUES, "Livro e Conhecimento", "Simboliza pergaminhos antigos", "e a luz da sabedoria escrita.", "O conhecimento e uma heranca", "eterna atraves das geracoes.")
                .addLore(Language.NEDERLANDS, "Boek & Kennis", "Symboliseert eeuwenoude rollen", "en het licht van geletterdheid.", "Kennis is een blijvend erfstuk", "doorgegeven aan generaties.")
                .addLore(Language.POLSKI, "Ksiega i Wiedza", "Symbolizuje starozytne zwoje", "oraz swiatlo nauki i pisma.", "Wiedza to wieczne dziedzictwo", "przekazywane pokoleniom.")
                .addLore(Language.TURKCE, "Kitap ve Bilim", "Kadim yazit rulolarini ve", "okuryazarlik isigini simgeler.", "Bilgi nesiller boyu aktarilan", "sonsuz bir mirastir.")
                .addLore(Language.SVENSKA, "Bok & Kunskap", "Symboliserar forntida bokrullar", "och bildningens klara ljus.", "Kunskap ar ett evigt arv som", "fors vidare genom generationer.")
                .addLore(Language.NORSK, "Bok & Kunnskap", "Symboliserer eldgamle bokruller", "og lærdommens klare lys.", "Kunnskap er en evig arv som", "gis videre gjennom slekter.")
                .addLore(Language.DANSK, "Bog & Viden", "Symboliserer oldtidens skriftruller", "og laerdommens klare lys.", "Viden er en evig arv der", "gives videre gennem generationer.")
                .addLore(Language.SUOMI, "Kirja & Tieto", "Symboloi muinaisia kaaroja", "ja oppineisuuden valoa.", "Tieto on ikuinen perinto,", "joka siirtyy sukupolvelta toiselle.")
                .addLore(Language.TAGALOG, "Aklat at Karunungan", "Sumasagisag sa sinaunang kasulatan", "at liwanag ng kaalaman.", "Ang karunungan ay walang hanggang", "pamana sa bawat salinlahi.")
                .addLore(Language.VIETNAMESE, "Sach & Tri Thuc", "Tuong trung cho cuon thu co", "va anh sang cua hoc van.", "Tri thuc la bau vat vinh cuu", "duoc truyen qua nhieu the he.")
                .addLore(Language.SWAHILI, "Kitabu na Maarifa", "Inaashiria maandishi ya kale", "na nuru ya elimu.", "Maarifa ni urithi wa milele", "unaopitishwa kizazi hadi kizazi."));

        entries.add(new RelicEntry(6, "ZHI")
                .addLore(Language.INDONESIA, "Kebijaksanaan & Aliran", "Aksara penghubung yang melambangkan", "keselarasan sebab dan akibat.", "Mengajarkan kebijaksanaan melihat", "keterkaitan setiap langkah kecil.")
                .addLore(Language.ENGLISH, "Wisdom & Flow", "A connective glyph signifying", "harmony of cause and effect.", "Teaches wisdom in seeing how", "small steps form a whole.")
                .addLore(Language.MELAYU, "Kebijaksanaan & Aliran", "Aksara penghubung yang melambangkan", "keselarasan sebab dan akibat.", "Mengajar kebijaksanaan melihat", "kaitan setiap langkah kecil.")
                .addLore(Language.JAWA, "Kawicaksanan & Ilir", "Aksara panggandheng kang nglambangake", "selarasing sabab lan akibat.", "Mulang kawicaksanan ndeleng", "sesambungane saben langkah cilik.")
                .addLore(Language.SUNDA, "Kawijaksanaan & Aliran", "Aksara panyambung anu ngalambangkeun", "kasaluyuan sabab jeung akibat.", "Ngajarkeun kawijaksanaan ningali", "patalina unggal lengkah leutik.")
                .addLore(Language.ESPANOL, "Sabiduria y Flujo", "Glifo conector que simboliza la", "armonia entre causa y efecto.", "Ensena a ver como cada pequeno", "paso forma un todo armonioso.")
                .addLore(Language.FRANCAIS, "Sagesse et Flux", "Glyphe de liaison symbolisant", "l'harmonie des causes et effets.", "Enseigne comment chaque petit", "pas construit un ensemble uni.")
                .addLore(Language.DEUTSCH, "Weisheit & Fluss", "Verbindendes Zeichen fuer die", "Harmonie von Ursache und Wirkung.", "Lehrt die Weisheit, wie kleine", "Schritte ein Ganzes bilden.")
                .addLore(Language.ITALIANO, "Saggezza e Flusso", "Glifo connettivo che simboleggia", "l'armonia tra causa ed effetto.", "Insegna a cogliere come ogni", "piccolo passo formi l'insieme.")
                .addLore(Language.PORTUGUES, "Sabedoria e Fluxo", "Glifo conector que simboliza a", "harmonia entre causa e efeito.", "Ensina a perceber como cada", "pequeno passo forma um todo.")
                .addLore(Language.NEDERLANDS, "Wijsheid & Stroom", "Verbindend teken dat staat voor", "harmonie van oorzaak en gevolg.", "Leert hoe elke kleine stap", "samen een geheel vormt.")
                .addLore(Language.POLSKI, "Madrosc i Przeplyw", "Znak laczacy symbolizujacy", "harmonie przyczyny i skutku.", "Uczy dostrzegac jak kazdy", "maly krok tworzy calosc.")
                .addLore(Language.TURKCE, "Bilgelik ve Akis", "Sebep ve sonuc uyumunu", "simgeleyen baglayici isaret.", "Her kucuk adimin butunu nasil", "olusturdugunu gormeyi ogretir.")
                .addLore(Language.SVENSKA, "Visdom & Flode", "Forbindande tecken for harmonin", "mellan orsak och verkan.", "Lar ut visdomen i att se hur", "sma steg bildar en helhet.")
                .addLore(Language.NORSK, "Visdom & Flyt", "Forbindende tegn som symboliserer", "harmoni mellom arsak og virkning.", "Laerer oss a se hvordan sma", "steg skaper en helhet.")
                .addLore(Language.DANSK, "Visdom & Flow", "Forbindende tegn der symboliserer", "harmoni mellem arsag og virkning.", "Laerer os at se hvordan sma", "skridt danner en helhed.")
                .addLore(Language.SUOMI, "Viisaus & Virtaus", "Yhdysmerkki, joka symboloi", "syyn ja seurauksen sopusointua.", "Opettaa nakemaan kuinka pienet", "askeleet muodostavat kokonaisuuden.")
                .addLore(Language.TAGALOG, "Karunungan at Daloy", "Simbolong nag-uugnay sa", "pagkakaisa ng sanhi at bunga.", "Nagtuturo kung paano bumubuo", "ng kabuuan ang maliliit na hakbang.")
                .addLore(Language.VIETNAMESE, "Tri Tue & Dong Chay", "Chu noi tuong trung cho su", "hai hoa giua nhan va qua.", "Day ta thay moi buoc nho", "deu ket noi thanh mot the thong nhat.")
                .addLore(Language.SWAHILI, "Hekima na Mtiririko", "Alama unganishi inayoashiria", "uwiano wa chanzo na matokeo.", "Hufundisha jinsi hatua ndogo", "zinavyounda ukamilifu."));

        entries.add(new RelicEntry(7, "SHANG")
                .addLore(Language.INDONESIA, "Cita-Cita Luhur", "Menunjukkan arah ke atas dan", "tekad untuk terus berkembang.", "Menginspirasi pemain menjaga", "standar tertinggi dalam berkarya.")
                .addLore(Language.ENGLISH, "Aspiration & Ascent", "Points upward toward progress", "and resolve to keep ascending.", "Inspires the seeker to uphold", "high standards in every endeavor.")
                .addLore(Language.MELAYU, "Cita-Cita Luhur", "Menunjukkan arah ke atas dan", "tekad untuk terus maju.", "Menginspirasi pemain mengekalkan", "mutu tertinggi dalam usaha.")
                .addLore(Language.JAWA, "Gegayuhan Luhur", "Nuduhake arah mendhuwur lan", "tekad kanggo terus maju.", "Nyengkuyung supaya tansah", "nggayuh tataran paling dhuwur.")
                .addLore(Language.SUNDA, "Cita-Cita Luhur", "Nuduhkeun arah ka luhur jeung", "tekad pikeun terus nanjak.", "Mere inspirasi pikeun ngajaga", "standar pangluhurna dina karya.")
                .addLore(Language.ESPANOL, "Aspiracion y Ascenso", "Senala hacia arriba, hacia el", "progreso y la superacion constante.", "Inspira a mantener los mas", "altos ideales en cada reto.")
                .addLore(Language.FRANCAIS, "Aspiration et Ascension", "Pointe vers le haut, symbole", "de progres et d'elevation.", "Inspire a viser toujours plus", "haut dans chaque entreprise.")
                .addLore(Language.DEUTSCH, "Streben & Aufstieg", "Zeigt nach oben zu Fortschritt", "und dem Willen zum Aufstieg.", "Inspiriert dazu, stets nach", "den hoechsten Zielen zu streben.")
                .addLore(Language.ITALIANO, "Aspirazione e Ascesa", "Indica l'alto, il progresso e", "la volonta di migliorarsi sempre.", "Ispira a mantenere gli standard", "piu elevati in ogni impresa.")
                .addLore(Language.PORTUGUES, "Aspiracao e Ascensao", "Aponta para o alto, rumo ao", "progresso e evolucao continua.", "Inspira a manter os mais altos", "padroes em cada conquista.")
                .addLore(Language.NEDERLANDS, "Ambitie & Stijging", "Wijst omhoog naar vooruitgang", "en de wil om te blijven groeien.", "Inspireert om altijd de hoogste", "standaard na te streven.")
                .addLore(Language.POLSKI, "Aspiracja i Wzlot", "Wskazuje kierunek w gore ku", "rozwojowi i ciaglemu postepowi.", "Inspiruje do utrzymywania", "najwyzszych standardow.")
                .addLore(Language.TURKCE, "Yukselis ve Ulku", "Yukariyi, ilerlemeyi ve surekli", "gelisme azmini gosterir.", "Her cabada en yuksek hedefe", "ulasma ilhami verir.")
                .addLore(Language.SVENSKA, "Stravan & Uppstigning", "Pekar uppat mot framsteg och", "viljan att ständigt utvecklas.", "Inspirerar till att halla hogsta", "standard i varje utmaning.")
                .addLore(Language.NORSK, "Ambisjon & Oppstigning", "Peker oppover mot fremgang og", "viljen til stadig utvikling.", "Inspirerer til a holde hoyeste", "standard i enhver innsats.")
                .addLore(Language.DANSK, "Straben & Opstigning", "Peger opad mod fremskridt og", "viljen til stadig udvikling.", "Inspirerer til at holde hoejeste", "standard i enhver indsats.")
                .addLore(Language.SUOMI, "Pyrkimys & Nousu", "Osoittaa ylospain kohti kehitysta", "ja tahtoa nousta korkeammalle.", "Kannustaa yllapitamaan korkeaa", "tasoa jokaisessa haasteessa.")
                .addLore(Language.TAGALOG, "Mataas na Mithiin", "Nakaturo paitaas tungo sa", "pag-unlad at patuloy na pag-angat.", "Nagbibigay-inspirasyon na abutin", "ang pinakamataas na pamantayan.")
                .addLore(Language.VIETNAMESE, "Chi Huong Cao Ca", "Chi huong len tren, tuong trung", "cho su tien bo va vuon len.", "Truyen cam hung giu vung", "tieu chuan cao nhat trong moi viec.")
                .addLore(Language.SWAHILI, "Matarajio na Kupanda", "Inaelekeza juu kuelekea maendeleo", "na azma ya kuendelea kukua.", "Huhamasisha kudumisha viwango", "vya juu katika kila juhudi."));

        entries.add(new RelicEntry(8, "XIA")
                .addLore(Language.INDONESIA, "Kerendahan Hati", "Menunjukkan arah ke bawah sebagai", "pengingat akan akar yang kuat.", "Kekuatan sejati lahir dari", "pijakan yang membumi.")
                .addLore(Language.ENGLISH, "Humility & Grounding", "Points downward as a reminder", "of deep and steady roots.", "True strength is born from", "staying humble and grounded.")
                .addLore(Language.MELAYU, "Kerendahan Hati", "Menunjukkan arah ke bawah sebagai", "peringatan tentang akar yang teguh.", "Kekuatan sejati lahir daripada", "pendirian yang membumi.")
                .addLore(Language.JAWA, "Andhap Asor", "Nuduhake arah mangisor minangka", "pangeling marang oyod kang kuwat.", "Kekuwatan sejati lair saka", "sikep kang andhap asor.")
                .addLore(Language.SUNDA, "Handap Asor", "Nuduhkeun arah ka handap pikeun", "ngingetan kana akar anu kuat.", "Kakuatan sajati lahir tina", "sikep anu handap asor.")
                .addLore(Language.ESPANOL, "Humildad y Arraigo", "Senala hacia abajo recordando", "la importancia de raices firmes.", "La verdadera fuerza nace de", "mantenerse humilde y centrado.")
                .addLore(Language.FRANCAIS, "Humilite et Ancrage", "Pointe vers le bas en rappel", "de racines profondes et solides.", "La vraie force nait de", "l'humilite et de l'ancrage.")
                .addLore(Language.DEUTSCH, "Demut & Erdung", "Zeigt nach unten als Mahnung", "an tiefe und feste Wurzeln.", "Wahre Staerke entsteht durch", "Bescheidenheit und Bodenhaftung.")
                .addLore(Language.ITALIANO, "Umilta e Radicamento", "Indica il basso ricordando", "il valore di radici profonde.", "La vera forza nasce dal", "restare umili e ben saldi.")
                .addLore(Language.PORTUGUES, "Humildade e Alicerce", "Aponta para baixo lembrando", "a forca de raizes profundas.", "A verdadeira forca nasce de", "permanecer humilde e firme.")
                .addLore(Language.NEDERLANDS, "Nederigheid & Basis", "Wijst omlaag als herinnering", "aan diepe en stevige wortels.", "Ware kracht ontstaat door", "nederig en geaard te blijven.")
                .addLore(Language.POLSKI, "Pokora i Zakorzenienie", "Wskazuje w dol przypominajac", "o glebokich i mocnych korzeniach.", "Prawdziwa sila rodzi sie z", "pokory i twardego gruntu.")
                .addLore(Language.TURKCE, "Alcakgonulluluk", "Derin ve saglam kokleri", "hatirlatmak icin asagiyi gosterir.", "Gercek guc alcakgonullu ve", "yere saglam basmaktan dogar.")
                .addLore(Language.SVENSKA, "Odmiukhet & Forankring", "Pekar nedat som en paminnelse", "om djupa och stadiga rotter.", "Sann styrka fods ur att", "forbli odmiuk och jordnara.")
                .addLore(Language.NORSK, "Ydmykhet & Forankring", "Peker nedover som en paminnelse", "om dype og stoe rotter.", "Sann styrke fodes av a", "forbli ydmyk og jordnaer.")
                .addLore(Language.DANSK, "Ydmyghed & Forankring", "Peger nedad som en pamindelse", "om dybe og staerke roedder.", "Sand styrke foedes af at", "forblive ydmyg og jordnaer.")
                .addLore(Language.SUOMI, "Noyryys & Juurevuus", "Osoittaa alaspain muistuttaen", "syvista ja vahvoista juurista.", "Todellinen voima syntyy", "noyryydesta ja vakaasta pohjasta.")
                .addLore(Language.TAGALOG, "Kababaang-Loob", "Nakaturo paibaba bilang paalala", "sa malalim at matibay na ugat.", "Ang tunay na lakas ay nagmumula", "sa pagiging mapagkumbaba.")
                .addLore(Language.VIETNAMESE, "Khiem Nhuong & Ben Re", "Chi huong xuong duoi nhac nho", "ve goc re sau ben vung chac.", "Suc manh chan chinh sinh ra", "tu su khiem ton va vung vang.")
                .addLore(Language.SWAHILI, "Unyenyekevu na Mizizi", "Inaelekeza chini kukumbusha", "umuhimu wa mizizi imara.", "Nguvu ya kweli huzaliwa kwa", "kuwa mnyenyekevu na thabiti."));

        entries.add(new RelicEntry(9, "SHI (II)")
                .addLore(Language.INDONESIA, "Generasi & Warisan", "Terbentuk dari tiga puluh tahun", "perjalanan satu generasi manusia.", "Melambangkan kesinambungan", "budaya dan warisan masa depan.")
                .addLore(Language.ENGLISH, "Generation & Legacy", "Formed from thirty years marking", "a human generation's span.", "Symbolizes cultural continuity", "and a lasting future legacy.")
                .addLore(Language.MELAYU, "Generasi & Warisan", "Terbentuk daripada tiga puluh", "tahun perjalanan satu generasi.", "Melambangkan kesinambungan", "budaya dan warisan masa depan.")
                .addLore(Language.JAWA, "Generasi & Warisan", "Kawangun saka telung puluh taun", "lakuning siji generasi manungsa.", "Nglambangake lestariing budaya", "lan warisan kanggo anak putu.")
                .addLore(Language.SUNDA, "Generasi & Warisan", "Diwangun tina tilu puluh taun", "lalampahan hiji generasi manusa.", "Ngalambangkeun kalanggengan", "budaya jeung warisan kahareup.")
                .addLore(Language.ESPANOL, "Generacion y Legado", "Formado por treinta anos que", "marcan una generacion humana.", "Simboliza continuidad cultural", "y un legado duradero.")
                .addLore(Language.FRANCAIS, "Generation et Heritage", "Forme de trente annees marquant", "la duree d'une generation.", "Symbolise la continuite", "culturelle et l'heritage futur.")
                .addLore(Language.DEUTSCH, "Generation & Vermaechtnis", "Gebildet aus dreissig Jahren,", "der Spanne einer Generation.", "Symbolisiert kulturelle Kontinuitaet", "und ein bleibendes Erbe.")
                .addLore(Language.ITALIANO, "Generazione ed Eredita", "Formato da trent'anni che", "segnano una generazione umana.", "Simboleggia continuita culturale", "e un'eredita duratura.")
                .addLore(Language.PORTUGUES, "Geracao e Legado", "Formado por trinta anos que", "marcam uma geracao humana.", "Simboliza continuidade cultural", "e um legado duradouro.")
                .addLore(Language.NEDERLANDS, "Generatie & Erfenis", "Gevormd uit dertig jaren, de", "spanne van een mensengeneratie.", "Symboliseert culturele continuiteit", "en een blijvende erfenis.")
                .addLore(Language.POLSKI, "Pokolenie i Dziedzictwo", "Utworzony z trzydziestu lat", "wyznaczajacych jedno pokolenie.", "Symbolizuje ciaglosc kultury", "i trwale dziedzictwo.")
                .addLore(Language.TURKCE, "Nesil ve Miras", "Bir insan neslini simgeleyen", "otuz yillik surecten olusur.", "Kulturel surekliligi ve gelecege", "birakilan mirasi temsil eder.")
                .addLore(Language.SVENSKA, "Generation & Arv", "Format av trettio ar som", "markerar en mansklig generation.", "Symboliserar kulturell kontinuitet", "och ett varaktigt framtida arv.")
                .addLore(Language.NORSK, "Generasjon & Arv", "Formet av tretti ar som", "markerer en menneskegenerasjon.", "Symboliserer kulturell kontinuitet", "og en varig fremtidig arv.")
                .addLore(Language.DANSK, "Generation & Arv", "Formet af tredive ar der", "markerer en menneskegeneration.", "Symboliserer kulturel kontinuitet", "og en varig fremtidig arv.")
                .addLore(Language.SUOMI, "Sukupolvi & Perinto", "Muodostuu kolmestakymmenesta", "vuodesta, yhden sukupolven ajasta.", "Symboloi kulttuurin jatkuvuutta", "ja kestavaa tulevaisuuden perintoa.")
                .addLore(Language.TAGALOG, "Henerasyon at Pamana", "Binubuo ng tatlumpung taon na", "sumasagisag sa isang henerasyon.", "Tanda ng pagpapatuloy ng kultura", "at pangmatagalang pamana.")
                .addLore(Language.VIETNAMESE, "The He & Di San", "Tao thanh tu ba muoi nam,", "dau moc cua mot the he con nguoi.", "Tuong trung cho su tiep noi", "van hoa va di san ben vung.")
                .addLore(Language.SWAHILI, "Kizazi na Urithi", "Imeundwa kutokana na miaka", "thelathini ya kizazi kimoja.", "Inaashiria mwendelezo wa", "utamaduni na urithi wa baadaye."));

        entries.add(new RelicEntry(10, "ZHONG")
                .addLore(Language.INDONESIA, "Keseimbangan Tengah", "Garis tegak membelah kotak tepat", "di tengah, lambang keadilan.", "Mengajarkan jalan tengah yang", "seimbang dalam tiap keputusan.")
                .addLore(Language.ENGLISH, "Center & Balance", "A vertical line piercing the", "exact center, symbol of fairness.", "Teaches the golden mean of", "balance in every decision.")
                .addLore(Language.MELAYU, "Keseimbangan Tengah", "Garis tegak membelah kotak tepat", "di tengah, lambang keadilan.", "Mengajar jalan tengah yang", "seimbang dalam setiap keputusan.")
                .addLore(Language.JAWA, "Kaseimbangan Tengah", "Garis jejeg nyigar kothak pas", "ing tengah, lambang kaadilan.", "Mulang dalan tengah kang", "seimbang ing saben putusan.")
                .addLore(Language.SUNDA, "Kasaimbangan Tengah", "Garis ajeg meulah kotak pas", "di tengah, lambang kaadilan.", "Ngajarkeun jalan tengah anu", "saimbang dina unggal kaputusan.")
                .addLore(Language.ESPANOL, "Centro y Equilibrio", "Linea vertical que cruza el", "centro exacto, simbolo de justicia.", "Ensena el justo medio y el", "equilibrio en cada decision.")
                .addLore(Language.FRANCAIS, "Centre et Equilibre", "Ligne verticale traversant le", "centre exact, symbole de justice.", "Enseigne le juste milieu et", "l'equilibre dans chaque choix.")
                .addLore(Language.DEUTSCH, "Mitte & Gleichgewicht", "Senkrechte Linie genau durch", "die Mitte, Symbol der Gerechtigkeit.", "Lehrt die goldene Mitte und", "Ausgewogenheit jeder Entscheidung.")
                .addLore(Language.ITALIANO, "Centro ed Equilibrio", "Linea verticale che attraversa", "il centro esatto, simbolo di equita.", "Insegna il giusto mezzo e", "l'equilibrio in ogni scelta.")
                .addLore(Language.PORTUGUES, "Centro e Equilibrio", "Linha vertical que atravessa", "o centro exato, simbolo de justica.", "Ensina o caminho do meio e o", "equilibrio em cada decisao.")
                .addLore(Language.NEDERLANDS, "Midden & Balans", "Verticale lijn precies door het", "midden, symbool van rechtvaardigheid.", "Leert de gulden middenweg en", "balans bij elke beslissing.")
                .addLore(Language.POLSKI, "Srodek i Rownowaga", "Pionowa linia przecinajaca", "dokladny srodek, znak sprawiedliwosci.", "Uczy zlotego srodka oraz", "rownowagi w kazdej decyzji.")
                .addLore(Language.TURKCE, "Merkez ve Denge", "Tam ortadan gecen dikey cizgi,", "adalet ve uyumun simgesidir.", "Her kararda olcululugu ve", "denge yolunu ogretir.")
                .addLore(Language.SVENSKA, "Centrum & Balans", "En lodrat linje genom exakt", "mitten, symbol for rattvisa.", "Lar ut den gyllene medelvagen", "och balans i varje beslut.")
                .addLore(Language.NORSK, "Sentrum & Balanse", "En loddrett linje gjennom", "midten, symbol pa rettferdighet.", "Laerer den gylne middelvei", "og balanse i hver beslutning.")
                .addLore(Language.DANSK, "Centrum & Balance", "En lodret linje gennem praecis", "midten, symbol pa retfaerdighed.", "Laerer den gyldne middelvej", "og balance i enhver beslutning.")
                .addLore(Language.SUOMI, "Keskusta & Tasapaino", "Pystyviiva halkoo keskipisteen", "tarkasti, oikeudenmukaisuuden merkki.", "Opettaa kultaisen keskitien ja", "tasapainon jokaisessa paatoksessa.")
                .addLore(Language.TAGALOG, "Gitna at Balanse", "Patayong guhit sa eksaktong", "gitna, sagisag ng katarungan.", "Nagtuturo ng balanseng daan", "sa bawat pagpapasya.")
                .addLore(Language.VIETNAMESE, "Trung Dung & Can Bang", "Net thang xuyen qua chinh giua,", "bieu tuong cua su cong bang.", "Day dao trung dung va su", "can bang trong moi quyet dinh.")
                .addLore(Language.SWAHILI, "Katikati na Usawa", "Mstari wima unaopita katikati", "kabisa, ishara ya haki.", "Hufundisha njia ya kati yenye", "usawa katika kila uamuzi."));
    }

    public static PusakaCompendium getInstance() {
        if (instance == null) {
            instance = new PusakaCompendium();
        }
        return instance;
    }

    public void recordClearedTiles(int[] clearedEncodedTileIds) {
        if (clearedEncodedTileIds == null) {
            return;
        }
        for (int encoded : clearedEncodedTileIds) {
            int baseId = SpecialTileType.extractBaseTileId(encoded);
            if (baseId > 0) {
                int idx = Math.floorMod(baseId - 1, entries.size());
                entries.get(idx).addCleared(1);
            }
        }
    }

    public List<RelicEntry> getEntries() {
        return entries;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public RelicEntry getCurrentEntry() {
        return entries.get(currentPageIndex);
    }

    public void nextPage() {
        currentPageIndex = Math.floorMod(currentPageIndex + 1, entries.size());
    }

    public void previousPage() {
        currentPageIndex = Math.floorMod(currentPageIndex - 1, entries.size());
    }

    public int getUnlockedCount() {
        int count = 0;
        for (RelicEntry e : entries) {
            if (e.isUnlocked()) {
                count++;
            }
        }
        return count;
    }
}