;;; Sierra Script 1.0 - (do not remove this comment)
;
;	MAIN.SC
;
;	This is the main game script. It contains the main game class, all the global variables, and
;	a number of useful procedures.
;
;

(script# MAIN)
(include game.sh) (include menu.sh)
(use Intrface)
(use DCIcon)
(use LoadMany)
(use StopWalk)
(use Sound)
(use Save)
(use Motion)
(use Game)
(use Invent)
(use User)
(use Menu)
(use System)

(public
	SCI01 0 ;Replace "SCI01" with the game's internal name here (up to 6 characters)
	AnimateCast 1
	HandsOn 2
	HandsOff 3
	NormalEgo 4
	cls 5
	Btst 6
	Bset 7
	Bclr 8
	SolvePuzzle 9
	EgoDead	10
	PrintDontHaveIt 11
	PrintAlreadyDoneThat 12
	PrintNotCloseEnough 13
)

(local
	ego							;pointer to ego
	theGame						;ID of the Game instance
	curRoom						;ID of current room
	speed =  6					;The number of ticks between animations. This is set, usually as a menu
								;	option, to determine the speed of animation. The default is 6.
	quit						;when TRUE, quit game
	cast						;collection of actors
	regions						;set of current regions
	timers						;list of timers in the game
	sounds						;set of sounds being played
	inventory					;set of inventory items in game
	addToPics					;list of views added to the picture
	curRoomNum					;current room number
	prevRoomNum					;previous room number
	newRoomNum					;number of room to change to
	debugOn						;generic debug flag -- set from debug menu
	score						;the player's current score
	possibleScore				;highest possible score
	showStyle =  IRISOUT		;The global style for the transition from one picture to another.  This
     							;   may be overridden by the style property of a given room.  See the
     							;   DrawPic kernel function for the possible styles.
	aniInterval					;The number of timer ticks more than the Game's speed which it took to
     							;   complete the last animation cycle.  A non-zero aniInterval means that the
     							;   system is not keeping up.
	theCursor						;the number of the current cursor
	normalCursor = ARROW_CURSOR		;number of normal cursor form
	waitCursor	 = HAND_CURSOR		;cursor number of "wait" cursor
	userFont	 = USERFONT			;font to use for Print
	smallFont	 = 4				;small font for save/restore, etc.
	lastEvent					;the last event (used by save/restore game)
	modelessDialog				;the modeless Dialog known to User and Intrface
	bigFont =  USERFONT			;large font
	volume =  12				;last volume level set (from 0 to 15, 0 being off, 15 being loudest)
	version						;pointer to 'incver' version string
                                ;   WARNING!  Must be set in room 0
                                ;   (usually to {x.yyy    } or {x.yyy.zzz})
	locales
	[curSaveDir 20]			;current save drive/directory string [20 chars long]
	aniThreshold =  10
	perspective				;player's viewing angle: degrees away from vertical along y axis
	features				;locations that may respond to events
	sortedFeatures          ;requires SORTCOPY (script 984)
	useSortedFeatures		;enable cast & feature sorting?
	demoScripts				
	egoBlindSpot			;actors behind ego within angle 
							;from straight behind. 
							;Default zero is no blind spot
	overlays =  -1
	doMotionCue				;a motion cue has occurred - process it
	systemWindow			;ID of standard system window
	demoDialogTime =  3
	currentPalette
	modelessPort
	[sysLogPath 10]			;used for system standard logfile path (uses 10 globals)
	ftrInitializer			;pointer to code that gets called from
							;a feature's init
	doVerbCode				;pointer to code that gets invoked if
							;no feature claims a user event
	firstSaidHandler		
	useObstacles =  FALSE	;will Ego use PolyPath or not? (default is FALSE)
	;77 to 99 are unused
	global77
	global78
	global79
	global80
	global81
	global82
	global83
	global84
	global85
	global86
	global87
	global88
	global89
	global90
	global91
	global92
	global93
	global94
	global95
	global96
	global97
	global98
	lastSysGlobal
	;globals 100 and above are for game use
	isHandsOff
	deathMusic	= sDeath	;default death music
	musicChannels
	global103
	debugging	;debug mode enabled
	detailLevel		;detail level (0 = low, 1 = mid, 2 = high, 3 = ultra)
	theMusic			;music object, current playing music
	colorCount
	speedCount		;used to test how fast the system is
					;and used in determining detail level. (used in conjunction with detailLevel)
	cIcon
	soundFx				;sound effect being played
	[gameFlags 10]	;each global can have 16 flags. 10 globals * 16 flags = 160 flags. If you need more flags, just increase the array!
	curTextColor ;color of text in message boxes
	curBackColor ;color of message boxes
)

(procedure (AnimateCast)
	;Used to animate the cast, generally in a room's init() method.
	(Animate (cast elements?) FALSE)
)

(procedure (HandsOn)
	;Enable ego control
	(= isHandsOff FALSE)
	(User canControl: TRUE canInput: TRUE)
	(theGame setCursor: normalCursor (HaveMouse))
)

(procedure (HandsOff)
	;Disable ego control
	(= isHandsOff TRUE)
	(User canControl: FALSE canInput: FALSE)
	(theGame setCursor: waitCursor TRUE)
	(ego setMotion: 0)
)

(procedure (NormalEgo)
	;normalizes ego's animation
	(ego
		setLoop: -1
		setPri: -1
		setMotion: 0
		setCycle: StopWalk vEgoStand
		illegalBits: cWHITE
		cycleSpeed: 0
		moveSpeed: 0
		setStep: 3 2
		ignoreActors: 0
		looper: 0
	)
)

(procedure (cls)
	;Clear modeless dialog from the screen
	(if modelessDialog (modelessDialog dispose:))
)

(procedure (Btst flagEnum)
	;Test a boolean game flag
	(& [gameFlags (/ flagEnum 16)] (>> $8000 (mod flagEnum 16)))
)

(procedure (Bset flagEnum  &tmp oldState)
	;Set a boolean game flag
	(= oldState (Btst flagEnum))
	(|= [gameFlags (/ flagEnum 16)] (>> $8000 (mod flagEnum 16)))
	oldState
)

(procedure (Bclr flagEnum  &tmp oldState)
	;Clear a boolean game flag
	(= oldState (Btst flagEnum))
	(&= [gameFlags (/ flagEnum 16)] (~ (>> $8000 (mod flagEnum 16))))
	oldState
)

(procedure (SolvePuzzle flag points)
	;Adds an amount to the player's current score. A flag (one used with
	;Bset, Bclr, and Btst) is used so that a score is only added once.
		(if (not (Btst flag))
		(theGame changeScore: points)
		(Bset flag)
	)
)		

(procedure (EgoDead &tmp printRet)
	;This procedure handles when Ego dies. It closely matches that of QFG1EGA.
	;To use it: "(EgoDead {death message})".
	;You can add a title and icon in the same way as a normal Print message.
	(HandsOff)
	(Wait 100)
	(= normalCursor ARROW_CURSOR)
	(theGame setCursor: normalCursor TRUE)
	(soundFx stop:)
	(theMusic number: deathMusic play:)
		(repeat
			(= printRet
				(Print
					&rest
					#width 250
					#button	{Restore} 1
					#button {Restart} 2
					#button {__Quit__} 3
				)
			)
				(switch printRet
					(1
						(theGame restore:)
					)
					(2
						(theGame restart:)
					)
					(3
						(= quit TRUE) (break)
					)
				)
		)
)
(procedure (PrintDontHaveIt)
	(Print "You don't have it.")
)

(procedure (PrintAlreadyDoneThat)
	(Print "You've already done that.")
)

(procedure (PrintNotCloseEnough)
	(Print "You're not close enough.")
)

(instance egoObj of Ego
	(properties
		name "ego"
	)
)

;VerbCode and GameVerbMessager can be customized for additional verbs
;that are not part of the stock USER.SC. This was used for QFG2.

(instance VerbCode of Code
	(properties)
	
	(method (doit description theVerb &tmp [str 100])
		(switch theVerb
			(verbLook
				(if (description lookStr?)
					(Print (description lookStr?))
				else
					(Print (Format @str "Why, look! It's %s." (description description?)))
				)
			)
			(verbOpen
				(Print (Format @str "You can't open %s." (description description?)))
			)
			(verbClose
				(Print (Format @str "You can't close %s." (description description?)))
			)
			(verbSmell
				(Print (Format @str "To you, %s has no distinct smell." (description description?)))
			)
			(verbMove
				(Print (Format @str "You can't move %s." (description description?)))
			)
			(verbEat
				(Print (Format @str "Don't be silly, you can't eat %s!" (description description?)))
			)
			(verbGet
				(Print (Format @str "You can't get %s." (description description?)))
			)
			(verbClimb
				(Print (Format @str "You can't climb %s." (description description?)))
			)
			(verbTalk
				(Print (Format @str "Don't bother trying to talk to %s." (description description?)))
			)
		)
	)
)

(class GameVerbMessager of Code
	(properties
		ssLook 0
		ssOpen 0
		ssClose 0
		ssSmell 0
		ssMove 0
		ssEat 0
		ssGet 0
		ssClimb 0
		ssTalk 0
	)
	
	(method (doit)
		(return
			(cond 
				((Said ssLook) verbLook)
				((Said ssOpen) verbOpen)
				((Said ssClose) verbClose)
				((Said ssSmell) verbSmell)
				((Said ssMove) verbMove)
				((Said ssEat) verbEat)
				((Said ssGet) verbGet)
				((Said ssClimb) verbClimb)
				((Said ssTalk) verbTalk)
			)
		)
	)
)

(instance verbWords of GameVerbMessager
	(properties
		ssLook 'look>'
		ssOpen 'open>'
		ssClose 'close>'
		ssSmell 'smell>'
		ssMove 'move>'
		ssEat 'eat>'
		ssGet 'get,(pick<up)>'
		ssClimb 'climb,scale>'
		ssTalk 'talk>'
	)
)

(instance statusCode of Code
	(properties)
	
	(method (doit strg)
		(Format strg "___Template Game__________________Score: %d of %d" score possibleScore)
	)
)

(instance music of Sound
	(properties
		number 10
	)
)

(instance SFX of Sound
	(properties
		number 10
		priority 15
	)
)

(instance deathIcon of DCIcon
	(properties)
)


(instance Test_Object of InvItem
	(properties
		name {Test Object}
		description {This is a test object.}
		owner 0
		view vTestObject
		loop 0
		cel 0
	)
)

(instance SCI01 of Game ;Replace "SCI01" with the game's internal name here (up to 6 characters)
	; The main game instance. It adds game-specific functionality.
	(properties
		;Set your game's language here.
		;Supported langauges can be found in SYSTEM.SH.		
		parseLang ENGLISH
		printLang ENGLISH
	)
	
	(method (init)
		;set up various aspects of the game
		(= debugging TRUE) ;Set to TRUE if you want to enable the debug features.	
		(SysWindow
			;These colors can be changed to suit your preferences.
			;They can also be changed in the game's menu, like in LSL3.
			color: (= curTextColor vBLACK)
			back: (= curBackColor vWHITE)
		)
		(= colorCount (Graph GDetect))
		(= systemWindow SysWindow)
		(super init:)
		(= musicChannels (DoSound NumVoices))
		(= useSortedFeatures FALSE)
		(= cIcon deathIcon)
		(= ego egoObj)
		(= possibleScore 0)	;Set the maximum score here
		(= version {x.yyy.zzz}) ;set game version here
		(User alterEgo: ego verbMessager: verbWords)
		(= showStyle IRISIN)
		(= doVerbCode VerbCode)

		(TheMenuBar init: draw: hide:)
		(StatusLine code: statusCode disable:) ;hide the status code at startup
		(StopWalk init:)
		(if debugging
			(self setCursor: normalCursor (HaveMouse) 300 170)
		else
			(HandsOff)
			(self setCursor: normalCursor FALSE 350 200)
		)
		((= theMusic music) number: 10 owner: self init:)
		((= soundFx SFX) number: 10 owner: self init:)
		(inventory add:
			;Add your inventory items here. Make sure they are in the same order as the item list in GAME.SH.
				Test_Object
		)
		;and finally, now that the game's been initialized, we can move on to the speed tester.
		(self newRoom: SPEEDTEST)
	)
	
	(method (doit)
		(super doit:)
	)

	(method (replay)
		(TheMenuBar draw:)
		(StatusLine enable:)
		(SetMenu soundI p_text
			(if (DoSound SoundOn) { Sound off} else { Sound on})
		)
		(super replay:)
	)	
	
	(method (newRoom)
		(super newRoom: &rest)
	)
	
	(method (startRoom roomNum)
		(LoadMany FALSE	
			;These are all disposed when going to another room, to reduce the
			;chances of "Memory Fragmented" errors.
			EXTRA FILE QSOUND GROOPER FORCOUNT SIGHT DPATH MOVEFWD JUMP SMOOPER
			REVERSE CHASE FOLLOW WANDER POLYPATH BLOCK PRINTD EXTRA
			APPROACH AVOIDER POLYGON TIMER QSOUND
		)
		(if debugging
			(if
				(and
					;if memory is fragmented and debugging is on, bring up a warning and internal debugger
					(u> (MemoryInfo FreeHeap) (+ 20 (MemoryInfo LargestPtr)))
					(Print "Memory fragmented." #button {Debug} TRUE)
				)
				(SetDebug)
			)
			(User canInput: TRUE)
		)
		(NormalEgo)
		(super startRoom: roomNum)
	)	
	
	(method (handleEvent event)
		(if (event claimed?)
			(return)
		)
		(super handleEvent: event)
		(switch (event type?)
			;Add global parser commands here.
			(saidEvent
				(cond
					((Said 'die') ;this shouldn't be in your game; it's just used to test the EgoDead procedure.
						(EgoDead "It's all over for now. Please try again." #title {You're dead.})
					)
					((Said 'cheat')
						(Print "Okay, you win.")
						(Print "(Game over.)" #at -1 152)
						(= quit TRUE)
					)
				)
			)
		)
		(if debugging
			(if
				(and
					(== (event type?) mouseDown)
					(& (event modifiers?) shiftDown)
				)
				(if (not (User canInput:))
					(event claimed: TRUE)
				else
					(cast eachElementDo: #handleEvent event)
					(if (event claimed?)
						(return)
					)
				)
			)
			(switch (event type?)
				(keyDown
					((ScriptID DEBUG) handleEvent: event)
				)
				(mouseDown
					((ScriptID DEBUG) handleEvent: event)
				)
			)
		)
	)
)