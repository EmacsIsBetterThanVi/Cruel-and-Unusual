from PyWigit import *
import CrueltyEngine
import os
# Setup
path = os.path.dirname(__file__)
screen = NewScreen((720, 600))
GameScreen = Screen(screen, FullScreen=True)
SetCaption("Cruel and Unusual")
font = NewFont("timesnewroman", 20)
# Title Screen
TITLE = NewFont("timesnewroman", 65).render("CRUEL AND UNUSUAL", True, Black)
CopyRight = Label("MÖRK BORG is copyright Ockult Örtmästare Games and Stockholm Kartell.", font, color=Black)
Logo = pygame.image.load_extended("Icon.png")
worldList = []
def startSinglePlayer():
    loadWorldList()
    GameScreen.ChangeScrn(2)
    Back.LeftClick = lambda: GameScreen.ChangeScrn(1)
singlePlayerButton = Button(font.render("Single Player", True, Black), LeftClick=startSinglePlayer)
def DrawScreen0(screen):
    singlePlayerButton.draw(screen, (100, 200))
    CopyRight.draw(screen, (20, 575))
    screen.blit(Logo, (120, 40))
    screen.blit(TITLE, (10, 0))
def Screen0Events(event):
    if singlePlayerButton.Click(event):
        return True
CreateScrn(DrawScreen0, Screen0Events, Yellow)
# World Selection Screen
wssa = ScrollArea(5, font.get_height(), [])
Back = Button(font.render("Back", True, Black), LeftClick=None)
def loadWorld(path):
    print(f"Loading world at {path}")
    Back.LeftClick = lambda: [(loadWorldList() if i==0 else GameScreen.ChangeScrn(2)) for i in range(2)]
def loadWorldList():
    global worldList
    worldList = glob.glob(path+"/*.world/")
    i=0
    wssa.display=[]
    for f in worldList:
        a=f.split('/')
        wssa.display.append(Button(font.render(a[len(a)-2].split('.')[0], True, Black), LeftClick=lambda: loadWorld(f)))
def DrawScreen1(screen):
    wssa.draw(screen, (100, 20))
    Back.draw(screen, (680, 0))
def Screen1Events(event):
    if wssa.Events(event):
        return True
    if Back.Click(event):
        return True
    for WORLD in wssa.display:
        if WORLD.Click(event):
            return True
CreateScrn(DrawScreen1, Screen1Events, Yellow)
# TODO: SCREENS
# Player Selection Screen
def DrawScreen2(screen):
    pass
def Screen2Events(event):
    pass
CreateScrn(DrawScreen2, Screen2Events, Yellow)
# LAN world select
def DrawScreen3(screen):
    pass
def Screen3Events(event):
    pass
CreateScrn(DrawScreen3, Screen3Events, Yellow)
# Create World
def DrawScreen4(screen):
    pass
def Screen4Events(event):
    pass
CreateScrn(DrawScreen4, Screen4Events, Yellow)
# Settings/Lisence
slsa = ScrollArea(5, font.get_height(), [])
def DrawScreen5(screen):
    sl5sa.draw(screen, (0, 0))
def Screen5Events(event):
    pass
CreateScrn(DrawScreen5, Screen5Events, Yellow)
# GAME
def DrawScreen6(screen):
    pass
def Screen6Event(event):
    pass
def Screen6FastTick():
    pass
CreateScrn(DrawScreen6, Screen1Events, Yellow, FastTick=Screen6FastTick)
while Status():
    MainLoop(screen, Fast=True)
pygame.quit()
