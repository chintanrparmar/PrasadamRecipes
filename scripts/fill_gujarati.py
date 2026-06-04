#!/usr/bin/env python3
"""Fill proper Gujarati content for new recipes (21-120).
Vocabulary is extracted at runtime from original properly-translated recipes (1-6).
Additional terms are built from known Gujarati Unicode code points.
"""
import json, os

base = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
f_path = os.path.join(base, "app/src/main/assets/recipes.json")

with open(f_path) as f:
    data = json.load(f)

orig = {r['slug']: r for r in data['recipes']}
r0 = orig['sabudana-khichdi']   # has correct Gujarati
r1 = orig['methi-thepla']       # has correct Gujarati
r2 = orig['gujarati-kadhi']     # has correct Gujarati
r4 = orig['kheer']              # has correct Gujarati
r5 = orig['aloo-gobi']         # has correct Gujarati

def w(s, i):               return s.split()[i]
def ws(s, a=None, b=None): return ' '.join(s.split()[a:b])
def vb(s):                 return s.rstrip('.').rstrip(',').split()[-1].rstrip('.').rstrip(',')

# ── Gujarati Unicode building blocks ─────────────────────────────────────────
_KA = chr(0x0A95); _KHA = chr(0x0A96); _GA = chr(0x0A97); _GHA = chr(0x0A98)
_CA = chr(0x0A9A); _CHA = chr(0x0A9B); _JA = chr(0x0A9C)
_TTA = chr(0x0A9F); _DDA = chr(0x0AA1); _NNA = chr(0x0AA3)
_TA = chr(0x0AA4); _THA = chr(0x0AA5); _DA = chr(0x0AA6); _DHA = chr(0x0AA7); _NA = chr(0x0AA8)
_PA = chr(0x0AAA); _PHA = chr(0x0AAB); _BA = chr(0x0AAC); _BHA = chr(0x0AAD); _MA = chr(0x0AAE)
_YA = chr(0x0AAF); _RA = chr(0x0AB0); _LA = chr(0x0AB2); _LLA = chr(0x0AB3)
_VA = chr(0x0AB5); _SHA = chr(0x0AB6); _SSA = chr(0x0AB7); _SA = chr(0x0AB8); _HA = chr(0x0AB9)
_AA  = chr(0x0ABE); _I  = chr(0x0ABF); _II = chr(0x0AC0); _U  = chr(0x0AC1); _UU = chr(0x0AC2)
_E   = chr(0x0AC7); _AI = chr(0x0AC8); _O  = chr(0x0ACB); _AU = chr(0x0ACC)
_AM  = chr(0x0A82); _VIR = chr(0x0ACD)
_A_V = chr(0x0A85); _AA_V = chr(0x0A86); _U_V = chr(0x0A89)

# ── Runtime-extracted Gujarati terms from original recipes ────────────────────
cap_gu        = w(r0['ingredients']['gu'][0], 1)
camaci_gu     = w(r0['ingredients']['gu'][3], 1)
ghi_gu        = w(r0['ingredients']['gu'][3], 2)
jeera_gu      = w(r0['ingredients']['gu'][4], 2)
hari_mirch_gu = ws(r0['ingredients']['gu'][5], 1, 3).rstrip(',')
sendha_gu     = ws(r0['ingredients']['gu'][6], 2)
chini_gu      = w(r0['ingredients']['gu'][7], 2)
nimbu_ras_gu  = ws(r0['ingredients']['gu'][8], 1)
_no_g         = chr(0x0AA8) + chr(0x0ACB)  # Gujarati "no" possessive suffix
nimbu_gu      = w(r0['ingredients']['gu'][8], 1).split(_no_g)[0]

ghau_aata_gu  = ws(r1['ingredients']['gu'][0], 2)
methi_gu      = w(r1['ingredients']['gu'][1], 3)
dahi_gu       = w(r1['ingredients']['gu'][2], 2)
tel_gu        = w(r1['ingredients']['gu'][3], 2)
til_gu        = w(r1['ingredients']['gu'][4], 2)
haldi_gu      = w(r1['ingredients']['gu'][5], 2)
lal_mirch_gu  = ws(r1['ingredients']['gu'][6], 2)
dhaniya_pdr_gu = ws(r1['ingredients']['gu'][7], 2)
namak_taste_gu = r1['ingredients']['gu'][8]
namak_gu      = w(r1['ingredients']['gu'][8], 0)

besan_gu      = w(r2['ingredients']['gu'][1], 2)
pani_gu       = w(r2['ingredients']['gu'][2], 2)
gol_gu        = w(r2['ingredients']['gu'][3], -1)
rai_gu        = w(r2['ingredients']['gu'][6], 2)
hing_gu       = w(r2['ingredients']['gu'][8], -1)
kadi_patta_gu = ws(r2['ingredients']['gu'][9], 1)
lal_sukha_gu  = ws(r2['ingredients']['gu'][10], 1)
adu_gu        = w(r2['ingredients']['gu'][12], 2).rstrip(',')

basmati_rice_gu = ws(r4['ingredients']['gu'][0], 2, 4).rstrip(',')
dudh_gu       = w(r4['ingredients']['gu'][1], -1)
elachi_gu     = w(r4['ingredients']['gu'][3], -1)
kaju_combo_gu = w(r4['ingredients']['gu'][5], -1)
drakh_gu      = w(r4['ingredients']['gu'][6], -1)
if '-' in kaju_combo_gu:
    kaju_gu  = kaju_combo_gu.split('-')[0]
    badam_gu = kaju_combo_gu.split('-')[1]
else:
    kaju_gu  = kaju_combo_gu
    badam_gu = kaju_combo_gu

phulkobi_gu = w(r5['ingredients']['gu'][1], 2).rstrip(',')
batata_gu   = w(r5['ingredients']['gu'][0], 1).rstrip(',')
sabudana_gu = w(r0['ingredients']['gu'][0], 2)
mungfali_gu = w(r0['ingredients']['gu'][2], -1)
chokha_gu   = w(basmati_rice_gu, -1) if basmati_rice_gu and ' ' in basmati_rice_gu else \
              _CA + _O + _KHA + _AA

# Instruction verbs (stripped of trailing punctuation)
ane_gu      = w(r2['instructions']['gu'][0], 2)          # "ane" (and) from "dahi ane besan..."
karo_gu     = r2['instructions']['gu'][0].split('.')[0].split()[-1]  # karo
umero_gu    = vb(r2['instructions']['gu'][0])             # umero (add)
bhelvo_gu   = vb(r2['instructions']['gu'][1])             # bheḷavo (mix)
ukalo_gu    = vb(r2['instructions']['gu'][3])             # ukāḷo (boil)
vaghar_act  = vb(r2['instructions']['gu'][4])             # vaghāro (temper)
umari_gu    = w(r0['instructions']['gu'][3], 0)           # umarī (add, imperative)
sheko_gu    = vb(r0['instructions']['gu'][3])             # śeκo (fry/cook)
halavo_gu   = vb(r0['instructions']['gu'][5])             # halāvo (stir)
piraso_gu   = vb(r0['instructions']['gu'][6])             # pīraso (serve)
nakho_gu    = vb(r0['instructions']['gu'][2]).lstrip(',') # nākho (put/add)
vano_gu     = vb(r1['instructions']['gu'][2])             # vaṇo (roll)
seko_gu     = vb(r1['instructions']['gu'][3])             # śeκo (bake/roast)
# Additional phrases
sathegaram_gu = ws(r2['instructions']['gu'][5], -2)       # "garam pīraso" last 2 words
sathe_gu    = w(r2['instructions']['gu'][5], 2)           # "sāthe" (with)
garam_gu    = w(r0['instructions']['gu'][6], -2)          # "garam" (warm)

# ── Additional terms from code points ────────────────────────────────────────
panir_gu    = _PA + _NA + _II + _RA + _VIR
matar_gu    = _VA + _TTA + _AA + _NNA + _AA
palak_gu    = _PA + _AA + _LLA + _KA + _VIR
tameta_gu   = _TTA + _AA + _MA + _E + _TTA + _VIR
dhaniya_gu  = _DHA + _AA + _NNA + _AA
gajar_gu    = _GA + _AA + _JA + _RA + _VIR
kobhi_gu    = _KA + _O + _BA + _II
bhinda_gu   = _BA + _I + _AM + _DDA + _AA
ringan_gu   = _RA + _II + _AM + _GA + _NNA + _VIR
dudhi_gu    = _DA + _UU + _DHA + _II
capsicum_gu = _KA + _E + _PA + _SA + _II + _KA + _AM + _VIR
tindora_gu  = _TTA + _II + _AM + _DDA + _O + _RA + _AA
makhai_gu   = _MA + _KA + _AA + _II
maida_gu    = _MA + _AA + _II + _DA + _AA
suji_gu     = _SA + _UU + _JA + _II
rajma_gu    = _RA + _AA + _JA + _MA + _AA
chana_gu    = _CA + _NNA + _AA
loT_gu      = _LA + _O + _TTA + _VIR
garam_masalo_gu = _GA + _RA + _MA + ' ' + _MA + _SA + _AA + _LA + _O
khoya_gu    = _KHA + _O + _YA + _AA
cream_gu    = _KA + _RA + _II + _MA + _VIR
makhan_gu   = _MA + _AA + _KHA + _NNA + _VIR
bakri_gu    = _BA + _AA + _JA + _RA + _II
jowar_gu    = _JA + _UU + _VA + _AA + _RA + _VIR
pista_gu    = _PA + _II + _SA + _TTA + _AA
aata_gu     = _AA_V + _TTA + _AA
kuttu_gu    = _KA + _UU + _TTA + _UU
rajgira_gu  = _RA + _AA + _JA + _GA + _II + _RA + _AA
makhana_gu  = _MA + _AA + _KHA + _AA + _NNA + _AA
singhara_gu = _SA + _II + _AM + _GHA + _AA + _RA + _AA
sama_gu     = _MA + _O + _RA + _AI + _YA + _O
kali_mirch_gu = _KA + _AA + _LLA + _I + ' ' + _MA + _RA + _CA + _AA + _AM
kala_namak_gu = _KA + _AA + _LLA + _UU + ' ' + namak_gu
chat_masala_gu = _CA + _AA + _TTA + ' ' + _MA + _SA + _AA + _LA + _O
kasuri_methi_gu = _KA + _AA + _SA + _UU + _RA + _II + ' ' + methi_gu
valliyo_gu  = _VA + _AA + _LLA + _II + _YA + _O
ajmo_gu     = _AA_V + _JA + _MA + _O
kankha_gu   = _KA + _AA + _AM + _KHA + _AA
amli_gu     = _AA_V + _MA + _LA + _II
narial_gu   = _NA + _AA + _RA + _II + _YA + _E + _LLA + _VIR
chole_gu    = _CA + _O + _LA + _E
sev_gu      = _SA + _E + _VA + _VIR
masrm_gu    = _MA + _SA + _RA + _UU + _MA + _VIR
kesar_gu    = _KA + _E + _SA + _RA + _VIR
daal_gu     = _DA + _AA + _LLA + _VIR     # dāḷ (lentil/dal)
dal_gu      = daal_gu
bhuko_gu    = _BHA + _UU + _KA + _O       # bhūko (powder)
# connectors & particles
ane_word    = _A_V + _NA + _E             # "ane" = and (verified: U+0A85+0AA8+0AC7)
maa_gu      = _MA + _AA + _AM            # māṃ = in
par_gu      = _PA + _RA + _VIR           # par = on
sathe_word  = _SA + _AA + _THA + _E      # sāthe = with
garam_word  = _GA + _RA + _MA + _VIR    # garam = warm/hot
thakai_gu   = _THA + _AM + _DA + _AA + _II  # ṭhaṃḍāī
kapo_gu     = _KA + _AA + _PA + _O      # kāpo = cut
nakho_word  = _NA + _AA + _KHA + _O     # nākho = add/put
# bake/cook action words built from code points
banavo_gu   = _BA + _NA + _AA + _VA + _O  # banāvo = make
mukai_gu    = _MA + _UU + _KA + _O        # mūko = keep/place
# thadva = warm up
# Instruction particles
dhaniya_bhabhro = dhaniya_gu + ' ' + _BHA + _BHA + _RA + _AA + _VA + _II  # ধাণা ভভরাবী

# ── VOCAB list (longest Hindi → Gujarati) ────────────────────────────────────
VOCAB = [
    # ---- danda → period ----
    ("।", "."),
    # ---- long compounds ----
    ("जीरा-धनिया पाउडर",    dhaniya_pdr_gu),
    ("लाल मिर्च पाउडर",     lal_mirch_gu),
    ("हल्दी पाउडर",          haldi_gu),
    ("धनिया पाउडर",          dhaniya_gu + _no_g + ' ' + bhuko_gu),
    ("जीरा पाउडर",           jeera_gu + _no_g + ' ' + bhuko_gu),
    ("गरम मसाला",            garam_masalo_gu),
    ("चाट मसाला",            chat_masala_gu),
    ("पाव भाजी मसाला",       _PA + _AA + _VA + _VIR + ' ' + _BHA + _AA + _JA + _II + ' ' + _MA + _SA + _AA + _LA + _O),
    ("काला नमक",             kala_namak_gu),
    ("काली मिर्च",           kali_mirch_gu),
    ("कसूरी मेथी",           kasuri_methi_gu),
    ("अदरक पेस्ट",           adu_gu + ' ' + _PA + _E + _SA + _TTA + _AA),
    # ---- long ingredient phrases ----
    ("गेहूं का आटा",          ghau_aata_gu),
    ("बाजरे का आटा",         bakri_gu + _no_g + ' ' + loT_gu),
    ("मक्के का आटा",          makhai_gu + _no_g + ' ' + loT_gu),
    ("बासमती चावल",          basmati_rice_gu),
    ("सेंधा नमक",             sendha_gu),
    ("हरी मिर्च",             hari_mirch_gu),
    ("लाल मिर्च",             lal_mirch_gu),
    ("करी पत्ते",             kadi_patta_gu),
    ("नींबू का रस",           nimbu_ras_gu),
    ("स्वादानुसार नमक",      namak_taste_gu),
    ("काला चना",             _KA + _AA + _LLA + _AA + ' ' + chana_gu),
    ("हरा धनिया",             dhaniya_gu),
    ("हरी चटनी",             _HA + _RA + _II + ' ' + _CA + _TTA + _NNA + _II),
    ("नारियल का दूध",         narial_gu + chr(0x0AA8) + chr(0x0AC1) + ' ' + dudh_gu),
    ("नारियल",               narial_gu),
    # ---- vegetables ----
    ("साबूदाना",     sabudana_gu),
    ("फूलगोभी",     phulkobi_gu),
    ("पत्तागोभी",   kobhi_gu),
    ("शिमला मिर्च", capsicum_gu),
    ("तिंदोरा",     tindora_gu),
    ("मशरूम",       masrm_gu),
    ("मकई",         makhai_gu),
    ("टमाटर",       tameta_gu),
    ("पालक",        palak_gu),
    ("भिंडी",       bhinda_gu),
    ("बैंगन",       ringan_gu),
    ("लौकी",        dudhi_gu),
    ("खीरा",        _KA + _AA + _KA + _DDA + _II),
    ("गाजर",        gajar_gu),
    ("आलू",         batata_gu),
    ("मटर",         matar_gu),
    # ---- proteins ----
    ("पनीर",        panir_gu),
    ("राजमा",       rajma_gu),
    ("छोले",        chole_gu),
    ("चना",         chana_gu),
    # ---- nuts & dry fruits ----
    ("मूंगफली",     mungfali_gu),
    ("काजू",        kaju_gu),
    ("बादाम",       badam_gu),
    ("किशमिश",     drakh_gu),
    ("पिस्ता",      pista_gu),
    # ---- dairy & fats ----
    ("मक्खन",       makhan_gu),
    ("क्रीम",        cream_gu),
    ("खोया",        khoya_gu),
    ("दही",         dahi_gu),
    ("दूध",         dudh_gu),
    ("घी",          ghi_gu),
    # ---- dal / lentils ----
    ("तुअर दाल",    _TA + _UU + _VA + _E + _RA + _VIR + ' ' + dal_gu),
    ("मूंग दाल",    _MA + _UU + _AM + _GA + ' ' + dal_gu),
    ("चना दाल",     chana_gu + ' ' + dal_gu),
    ("मसूर दाल",    _MA + _SA + _UU + _RA + _VIR + ' ' + dal_gu),
    ("उड़द दाल",    _U_V + _DA + _VIR + ' ' + dal_gu),
    ("दाल",          dal_gu),
    # ---- grains & flours ----
    ("बासमती",      w(basmati_rice_gu, 0) if ' ' in basmati_rice_gu else basmati_rice_gu),
    ("चावल",        chokha_gu),
    ("बेसन",        besan_gu),
    ("मैदा",        maida_gu),
    ("सूजी",        suji_gu),
    ("रवा",         suji_gu),
    ("कुट्टू",      kuttu_gu),
    ("राजगिरा",    rajgira_gu),
    ("मखाना",      makhana_gu),
    ("सिंघाड़े",   singhara_gu),
    ("समा",         sama_gu),
    ("आटा",         aata_gu),
    ("ज्वार",       jowar_gu),
    ("मेथी",        methi_gu),
    ("तिल",         til_gu),
    # ---- spices ----
    ("इलायची",     elachi_gu),
    ("अदरक",       adu_gu),
    ("हींग",        hing_gu),
    ("राई",         rai_gu),
    ("जीरा",        jeera_gu),
    ("हल्दी",       haldi_gu),
    ("इमली",        amli_gu),
    ("अमचूर",       kankha_gu),
    ("सौंफ",        valliyo_gu),
    ("अजवाइन",     ajmo_gu),
    ("केसर",        kesar_gu),
    ("धनिया",       dhaniya_gu),
    ("नमक",         namak_gu),
    ("पाउडर",       bhuko_gu),
    # ---- sweeteners & extras ----
    ("चीनी",        chini_gu),
    ("गुड़",         gol_gu),
    ("सेव",         sev_gu),
    # ---- liquids ----
    ("नींबू",        nimbu_gu),
    ("पानी",         pani_gu),
    ("तेल",          tel_gu),
    # ---- connectors (sorted long-to-short) ----
    ("स्वादानुसार",  ws(namak_taste_gu, 1) if ' ' in namak_taste_gu else namak_taste_gu),
    ("के साथ",       sathe_word),
    ("साथ",          sathe_word),
    ("और",           ane_word),
    # ---- instruction verbs ----
    ("परोसें",       piraso_gu),
    ("मिलाएं",      bhelvo_gu),
    ("उबालें",      ukalo_gu),
    ("भूनें",       sheko_gu),
    ("पकाएं",       karo_gu),
    ("डालें",       nakho_gu),
    ("हिलाएं",      halavo_gu),
    ("बेलें",       vano_gu),
    ("सजाएं",       piraso_gu),
    ("गर्म करें",    garam_word + ' ' + karo_gu),
    ("करें",         karo_gu),
    ("बनाएं",       banavo_gu),
    ("रखें",         mukai_gu),
    ("काटें",        kapo_gu),
    ("गर्म",         garam_word),
    # ---- units ----
    ("चम्मच",       camaci_gu),
    ("कप",          cap_gu),
    ("ग्राम",       _GA + _RA + _AA + _MA + _VIR),
    ("मिनट",        _MA + _II + _NA + _II + _TTA + _VIR),
    ("सेकंड",       _SA + _E + _KA + _AM + _DA + _VIR),
    ("लीटर",        _LA + _II + _TTA + _RA + _VIR),
]

VOCAB.sort(key=lambda x: -len(x[0]))

def translate(text):
    for hi, gu in VOCAB:
        text = text.replace(hi, gu)
    return text

def translate_list(items):
    return [translate(s) for s in items]

fixed = 0
for r in data["recipes"]:
    if r["id"] < 21:
        continue
    r["ingredients"]["gu"] = translate_list(r["ingredients"]["hi"])
    r["instructions"]["gu"] = translate_list(r["instructions"]["hi"])
    if r.get("notes") and isinstance(r["notes"], dict) and r["notes"].get("hi"):
        r["notes"]["gu"] = translate(r["notes"]["hi"])
    fixed += 1

with open(f_path, "w", encoding="utf-8") as f:
    json.dump(data, f, ensure_ascii=False, indent=2)

print(f"Filled Gujarati for {fixed} new recipes using vocabulary substitution.")
