import pygame
import noise
import json
import random
def DiceNote(String):
    tmp = String.split("d")[1]
    if '+' in tmp:
        return [int(String.split("d")[0]), int(tmp.split("+")[0]), int(tmp.split("+")[1])]
    elif '-' in tmp:
        return [int(String.split("d")[0]), int(tmp.split("+")[0]), int(tmp.split("-")[1])]
    else:
        return [int(String.split("d")[0]), int(tmp), 0]
def Dice(num=1, die=20, bonus=0, adv=0):
    rolls = []
    for i in abs(adv):
        roll = bonus
        for j in range(num):
            roll = random.randint(1, die)
        rolls.append(roll)
    return min(rolls) if adv<0 else max(rolls)
def GenMaterial(DICT):
    return Material(DICT["H"], DICT["E"], DICT["MP"], DICT["BP"], DICT["PROPS"], DICT["IMG"])
class Material():
    def __init__(self, hardness, edge, melting_point, boiling_point, properties, img):
        self.hardness = hardness
        self.edge = edge
        self.melting_point = melting_point
        self.boiling_point = boiling_point
        self.properties = properties
        self.color = color
        self.image = pygame.image.load_extended(img)
    def breakp(self, force):
        if self.hardness <= force:
            return True
        elif self.evalProps(force, Type="BREAK"):
            return True
        else:
            return False
    def EvalProps(self, *args, Type=""):
        for i in self.properties:
            if Type in i["T"]:
                Behaviors[i["func"]](args)
def GenCreature(DICT):
    STR = DiceNote(DICT["STR"])
    TOUGH = DiceNote(Dict["TOUGH"])
    PRES = DiceNote(DICT["PRES"])
    AGIL = DiceNote(DICT["AGIL"])
    HP = DiceNote(DICT["HP"])
    return CreatureType(Behaviors[DICT["AI"]], DICT["props"], STR[0], TOUGH[0], AGIL[0], PRES[0], HP[0], STR[1], TOUGH[1], AGIL[1], PRES[1], HP[1], STR[2], TOUGH[2], AGIL[2], PRES[2], HP[2])
class Creature():
    def __init__(self, HP, S, T, A, P, AI, props):
        self.MAXHP = HP
        self.HP = HP
        self.STR = S
        self.AG = A
        self.PRE = P
        self.AI = AI
        self.props = props
        self.TOUGH = T
    def EvalProps(self, *args, Type=""):
        for i in self.props:
            if Type in i["T"]:
                Behaviors[i["func"]](args)
class CreatureType():
    def __init__(self, AI, props, Sn=3, Tn=3, An=3, Pn=3, Hn=1, Sd=6, Td=6, Ad=6, Pd=6, Hd=8, Sb=0, Tb=0, Ab=0, Pb=0, Hb=0):
        self.Sn = Sn
        self.Tn = Tn
        self.An = An
        self.Pn = Pn
        self.Hn = Hn
        self.Sd = Sd
        self.Td = Td
        self.Ad = Ad
        self.Pd = Pd
        self.Hd = Hd
        self.Sb = Sb
        self.Tb = Tb
        self.Ab = Ab
        self.Pb = Pb
        self.AI = AI
        self.Hb = Hb
        self.props = props
    def spawn(self, pos):
        Creature(Dice(self.Hn, self.Hd, self.Hb), Dice(self.Sn, self.Sd, self.Sb), Dice(self.Tn, self.Td, self.Tb), Dice(self.An, self.Ad, self.Ab), Dice(self.Pn, self.Pd, self.Pb), self.AI, self.props)
"""
Shapes are defined as classes and must handle everything themselves
The following methods must be defined:
__init__(self, Material)       # Create an instance of the object
draw(self) -> pygame.Surface   # Should return a surface 
attack(self) -> int            # Attacks with the item. Things which can not be used as weapons should return 0
                                     ranged weapons do not use this method, this is only for melee
use(self, list)                # Use the item. Ranged Weapons should have use(["RELOAD"]) as a method 
getInfo(self) -> dict          # Returns info about the item. The key "T" should say what the item is, new types
                                     can be defined using the Types key under PYTHON
Types are a single method with the following prototype:
T(Action=Str, list) -> Any where Action can equal one of:
 - Uses -> [Str]
 - Keys -> [(Key, Mod, Use)]
"""
Materials = {}
Creatures = {}
Behaviors = {}
Objects = {}
Modules = {}
Types = {}
def UnloadStaticData():
    global Behaviors, Materials, Creatures, Objects, Modules, Types
    for i in Modules:
        i.unload()
    Behaviors = {}
    Materials = {}
    Creatures = {}
    Objects = {}
    Modules = {}
    Types = {}
# Moduels for Cruel and Unusual have two forms: Json files for resource packs, and Python files for new behaviors
def LoadStaticData(Data):
    DICT = json.loads(Data)
    for i in DICT["PYTHON"].keys():
        Modules[i] = __import__(DICT["PYTHON"][i]["MODULE"])
        Modules[i].load()
        for j in DICT["PYTHON"][i]["Functions"]:
            Behaviors[i+":"+j] = getattr(Modules[i], j)
        for j in DICT["PYTHON"][i]["Shapes"]:
            Objects[i+":"+j]= getattr(Modules[i], j)
        for j in DICT["PYTHON"][i]["Types"]:
            Types[i+":"+j]= getattr(Modules[i], j)
    for i in DICT["Materials"].keys():
        Materials[i] = GenMaterial(DICT["Materials"][i])
    for i in DICT["Creatures"].keys():
        Creatures[i] = GenCreature(DICT["Creatures"][i])
