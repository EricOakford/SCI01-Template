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
(use Grooper)
(use Window)
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
	RedrawCast 1
	HandsOn 2
	HandsOff 3
	NormalEgo 4
	cls 5
	Btst 6
	Bset 7
	Bclr 8
	SolvePuzzle 9
	EgoDead 10
	DontHave 11
	AlreadyDone 12
	NotClose 13
	CantDo 14
	CantSee 15
	InitAddToPics 16
	InitFeatures 17
	MouseClaimed 18
	Face 19
)

(local
	ego									;pointer to ego
	theGame								;ID of the Game instance
	curRoom								;ID of current room
	speed =  6							;number of ticks between animations
	quit								;when TRUE, quit game
	cast								;collection of actors
	regions								;set of current regions
	timers								;list of timers in the game
	sounds								;set of sounds being played
	inventory							;set of inventory items in game
	addToPics							;list of views added to the picture
	curRoomNum							;current room number
	prevRoomNum							;previous room number
	newRoomNum							;number of room to change to
	debugOn								;generic debug flag -- set from debug menu
	score								;the player's current score
	possibleScore						;highest possible score
	showStyle	=		IRISOUT			;style of picture showing
	aniInterval							;# of ticks it took to do the last animation cycle
	theCursor							;the number of the current cursor
	normalCursor =		ARROW_CURSOR	;number of normal cursor form
	waitCursor	 =		HAND_CURSOR		;cursor number of "wait" cursor
	userFont	 =		USERFONT		;font to use for Print
	smallFont	 =		4				;small font for save/restore, etc.
	lastEvent							;the last event (used by save/restore game)
	modelessDialog						;the modeless Dialog known to User and Intrface
	bigFont		=		USERFONT		;large font
	volume		=		12				;sound volume
	version		=		{x.yyy.zzz}		;pointer to 'incver' version string			
	locales								;set of current locales
	[curSaveDir 20]						;address of current save drive/directory string
	aniThreshold	=	10
	perspective							;player's viewing angle:
										;	 degrees away from vertical along y axis
	features							;locations that may respond to events
	sortedFeatures          			;above+cast sorted by "visibility" to ego
	useSortedFeatures					;enable cast & feature sorting?
	demoScripts							;add to curRoomNum to find room demo script
	egoBlindSpot						;used by sortCopy to exclude
										;actors behind ego within angle 
										;from straight behind. 
										;Default zero is no blind spot
	overlays	=		-1
	doMotionCue							;a motion cue has occurred - process it
	systemWindow						;ID of standard system window
	demoDialogTime	=	3				;how long Prints stay up in demo mode
	currentPalette
	modelessPort
	[sysLogPath 10]						;used for system standard logfile path (uses 10 globals)
	ftrInitializer						;pointer to code that gets called from
										;a feature's init
	doVerbCode							;pointer to code that gets invoked if
										;no feature claims a user event
	firstSaidHandler		
	useObstacles =  TRUE				;will Ego use PolyPath or not?
	;globals 77-99 are unused
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
	isHandsOff				;ego can't be controlled
	deathMusic	=	sDeath	;music to play in EgoDead
	numColors				;Number of colors supported by graphics driver
	numVoices				;Number of voices supported by sound driver
	debugging				;debug mode enabled
	howFast					;machine speed level (0 = slow, 1 = medium, 2 = fast, 3 = fastest)
	machineSpeed			;used by the speed tester to test how fast the system is
							; and used in determining game speed. (used in conjunction with howFast)
	theMusic				;music object, current playing music
	soundFx					;sound effect being played
	cIcon					;global pointer to cycling icon
	[gameFlags FLAG_ARRAY]	;array of all event flags. It can be increased in GAME.SH.
	myTextColor				;color of text in message boxes
	myBackColor				;color of message boxes
	egoWalk					;pointer for ego's static Walk object
	egoStopWalk				;pointer for ego's static StopWalk object
	egoLooper				;pointer for ego's static Grooper object
	egoBaseSetter			;pointer for ego's baseSetter code
	keyDownHandler			;pointer for keyDown EventHandler
	directionHandler		;pointer for direction EventHandler
	mouseDownHandler		;pointer for mouseDown EventHandler
	oldSysTime				;previous value of system's real-time clock
	gameSeconds				;elapsed seconds
	gameMinutes				;elapsed minutes
	gameHours				;elapsed hours
	graphicsDriver
)

(procedure (RedrawCast)
	;Used to re-animate the cast without cycling
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
		looper: egoLooper
		setPri: -1
		setMotion: 0
		setCycle: egoStopWalk vEgoStand
		illegalBits: cWHITE
		baseSetter: egoBaseSetter
		ignoreActors: FALSE
	)
)

(procedure (cls)
	;Clear modeless dialog from the screen
	(if modelessDialog
		(modelessDialog dispose:)
	)
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

(procedure (SolvePuzzle flagEnum points)
	;Adds an amount to the player's current score. A flag (one used with
	;Bset, Bclr, and Btst) is used so that a score is only added once.
		(if (not (Btst flagEnum))
		(theGame changeScore: points)
		(Bset flagEnum)
	)
)		

(procedure (EgoDead)
	;This procedure handles when Ego dies. It closely matches that of QFG1EGA.
	;It's used in the same way as a normal Print message.
	(HandsOff)
	(Wait 100)
	(= normalCursor ARROW_CURSOR)
	(theGame setCursor: normalCursor TRUE)
	(soundFx stop:)
	(theMusic number: deathMusic play:)
	(repeat
		(switch
			(Print
				&rest
				#width 250
				#button	{Restore} 1
				#button {Restart} 2
				#button {__Quit__} 3
			)
			(1
				(theGame restore:)
			)
			(2
				(theGame restart:)
			)
			(3
				(= quit TRUE)
				(break)
			)
		)
	)
)

(procedure (DontHave)
	(Print "You don't have it.")
)

(procedure (AlreadyDone)
	(Print "You've already done that.")
)

(procedure (NotClose)
	(Print "You're not close enough.")
)

(procedure (CantDo)
	(Print "You can't do that now.")
)

(procedure (CantSee)
	(Print "You see nothing like that here.")
)

;These two procedures allow for adding multiple ATPs and features at a time.
;They were used in QFG2.
(procedure (InitAddToPics)
	(addToPics
		add: &rest
		eachElementDo: #init
		doit:
	)
)

(procedure (InitFeatures)
	(features
		add: &rest
		eachElementDo: #init
		doit:
	)
)

(procedure (MouseClaimed obj event shifts)
	;check for right-click on an object and claim the event
	(return
		(if (MousedOn obj event shifts)
			(event claimed: TRUE)
			(return TRUE)
		else
			(return FALSE)
		)
	)
)

(procedure (Face actor1 actor2 both whoToCue &tmp ang1To2)
	;make one actor face another
	(= ang1To2
		(GetAngle
			(actor1 x?)
			(actor1 y?)
			(actor2 x?)
			(actor2 y?)
		)
	)
	(if (and (>= argc 3) both)
		(actor2 setHeading: (+ 180 ang1To2))
	)
	(if (>= argc 4)
		(actor1 setHeading: ang1To2 whoToCue)
	else
		(actor1 setHeading: ang1To2)
	)
)

(instance egoObj of Ego
	(properties
		name "ego"
	)
)

(instance egoW of Walk)

(instance egoSW of StopWalk)

(instance egoGL of GradualLooper)

(instance egoBase of Code
	
	(method (doit theActor &tmp theX theY)
		(= theX (theActor x?))
		(= theY (+ 1 (theActor y?)))
		(theActor
			brTop: (- theY 2)
			brBottom: theY
			brLeft: (- theX 9)
			brRight: (+ theX 9)
		)
	)
)

(instance statusCode of Code
	
	(method (doit strg)
		(Format strg "___Template Game_______________Score: %d of %d" score possibleScore)
	)
)

(instance music of Sound
	(properties
		number sDeath
	)
)

(instance SFX of Sound
	(properties
		number sDeath
		priority 15
	)
)

(instance deathIcon of DCIcon)

(instance ftrInitCode of Code
	
	(method (doit obj)
		(if (== (obj sightAngle?) ftrDefault)
			(obj sightAngle: 180)
		)
		(if (== (obj closeRangeDist?) ftrDefault)
			(obj closeRangeDist: 500)
		)
		(if (== (obj longRangeDist?) ftrDefault)
			(obj longRangeDist: 500)
		)
		(if (== (obj shiftClick?) ftrDefault)
			(obj shiftClick: verbLook)
		)
		(if (== (obj contClick?) ftrDefault)
			(obj contClick: verbNone)
		)
		(if (== (obj actions?) ftrDefault)
			(obj actions: 0)
		)
		(if (== (obj control?) ftrDefault)
			(obj control: 0)
		)
		(if (== (obj verbChecks1?) ftrDefault)
			(obj verbChecks1: $bbb5)
		)
		(if (== (obj verbChecks2?) ftrDefault)
			(obj verbChecks2: $bbbb)
		)
		(if (== (obj verbChecks3?) ftrDefault)
			(obj verbChecks3: $bbbb)
		)
	)
)

;	This instance can be customized for additional verbs
;	that are not part of the stock USER.SC. This was done in Quest for Glory II.

(instance verbWords of VerbMessager
	(properties)
	
	(method (doit)
		(return
			(cond 
				((Said 'look>')
					verbLook
				)
				((Said 'open>')
					verbOpen
				)
				((Said 'close>')
					verbClose
				)
				((Said 'smell>')
					verbSmell
				)
				((Said 'move>')
					verbMove
				)
				((Said 'eat>')
					verbEat
				)
				((Said 'get,(pick<up)>')
					verbGet
				)
				((Said 'climb>')
					verbClimb
				)
				((Said 'talk>')
					verbTalk
				)
			)
		)
	)
)

(instance DoVerbCode of Code
	(properties)
	
	(method (doit theObj theVerb &tmp [str 100])
		(switch theVerb
			(verbLook
				(if (theObj lookStr?)
					(Print (theObj lookStr?))
				else
					(Print (Format @str "Why, look! It's %s." (theObj description?)))
				)
			)
			(verbOpen
				(Print (Format @str "You can't open %s." (theObj description?)))
			)
			(verbClose
				(Print (Format @str "You can't close %s." (theObj description?)))
			)
			(verbSmell
				(Print (Format @str "To you, %s has no distinct smell." (theObj description?)))
			)
			(verbMove
				(Print (Format @str "You can't move %s." (theObj description?)))
			)
			(verbEat
				(Print (Format @str "Don't be silly, you can't eat %s!" (theObj description?)))
			)
			(verbGet
				(Print (Format @str "You can't get %s." (theObj description?)))
			)
			(verbClimb
				(Print (Format @str "You can't climb %s." (theObj description?)))
			)
			(verbTalk
				(Print (Format @str "Don't bother trying to talk to %s." (theObj description?)))
			)
		)
	)
)

(instance keyH of EventHandler)		;get keyDown events

(instance dirH of EventHandler)		;get direction events

(instance mouseH of EventHandler)	;get mouseDown events

(instance SCI01 of Game ;Replace "SCI01" with the game's internal name here (up to 6 characters)
	; The main game instance. It adds game-specific functionality.
	
	(method (init)
		;load some important modules
		(= systemWindow SysWindow)
		Cycle
		StopWalk
		Window
		DCIcon
		TheMenuBar
		;set up various aspects of the game
		(super init:)
		(= cIcon deathIcon)
		(= ego egoObj)
		(= egoWalk egoW)
		(= egoStopWalk egoSW)
		(= egoLooper egoGL)
		(= egoBaseSetter egoBase)
		(= version {x.yyy.zzz}) ;set game version here
		(= doVerbCode DoVerbCode)
		((= keyDownHandler keyH) add:)
		((= directionHandler dirH) add:)
		((= mouseDownHandler mouseH) add:)
		(= useSortedFeatures TRUE)
		(= ftrInitializer ftrInitCode)
		(User alterEgo: ego verbMessager: verbWords)
		(TheMenuBar init: draw: hide: state: FALSE)
		(StatusLine code: statusCode disable:) ;hide the status line at startup
		(if debugging
			(self setCursor: normalCursor (HaveMouse) 300 170)
		else
			(HandsOff)
			(self setCursor: normalCursor FALSE 350 200)
		)
		((= theMusic music)
			number: sDeath
			owner: self
			init:
		)
		((= soundFx SFX)
			number: sDeath
			owner: self
			init:
		)
		(inventory add:
			;Add your inventory items here. Make sure they are in the same order as the item list in GAME.SH.
			Test_Object
		)
		;moved any code not requiring any objects in this script into its own script
		((ScriptID GAME_INIT 0) init:)
		;and finally, now that the game's been initialized, we can move on to the speed tester.
		(self newRoom: SPEED)
	)

	(method (doit &tmp thisTime)
		(super doit:)
		;let the game's clock tick
		(if (!= oldSysTime (= thisTime (GetTime SYSTIME1)))
			(= oldSysTime thisTime)
			(if (== 60 (++ gameSeconds))
				(= gameSeconds 0)
				(if (== 60 (++ gameMinutes))
					(= gameMinutes 0)
					(++ gameHours)
				)
			)
		)
	)

	(method (replay)
		(TheMenuBar draw:)
		(StatusLine enable:)
		((ScriptID GAME_INIT 1) doit:)	;set up the colors and sound again, in case the drivers were changed
		(DisposeScript GAME_INIT)		;then trash the script
		(SetMenu soundI p_text
			(if (DoSound SoundOn) {Sound off} else {Sound on})
		)
		(super replay:)
	)

	(method (startRoom roomNum)
		((ScriptID DISPOSE_CODE 0) doit:)
		(cls)
		(if debugging
			(if
				(and
					;if memory is fragmented and debugging is on, bring up a warning and the internal debugger
					(u> (MemoryInfo FreeHeap) (+ 20 (MemoryInfo LargestPtr)))
					(Print
						"Memory fragmented."
						#button {Debug} TRUE
					)
				)
				(SetDebug)
			)
			(User canInput: TRUE)
		)
		(mouseDownHandler add: cast features)
		(NormalEgo)
		(super startRoom: roomNum)
		(if debugging
			(curRoom setLocales: DEBUG)
		)
	)
	
	(method (handleEvent event &tmp i)
		(switch (event type?)
			(keyDown
				(keyDownHandler handleEvent: event)
			)
			(direction
				(directionHandler handleEvent: event)
			)
			(mouseDown
				(cond
					((mouseDownHandler handleEvent: event))
					((MouseClaimed ego event shiftDown)
						(Print "Why, that's me!")
					)
				)
			)
			(mouseUp
				(cast handleEvent: event)
			)
			(joyDown
				(event type: keyDown message: ENTER)
				(keyDownHandler handleEvent: event)
			)			
			;Add global parser commands here.
			(saidEvent
				(cond
					((super handleEvent: event)) ;for rooms, regions, and locales
					((Said 'cheat')
						(Print "Okay, you win.")
						(Print "(Game over.)" #at -1 152)
						(= quit TRUE)
					)
					((Said 'look[<at]>') ;look at inventory items
						(if (= i (inventory saidMe:))
							(if (i ownedBy: ego)
								(i showSelf:)
								else (DontHave)
							)
								;if not an inventory item
						else 	;this will handle "look anyword"
							(CantSee)
						)
					)
				)
			);end of saidEvents
		)
	)
)

(class GameInvItem of InvItem
	;this subclass will allow item descriptions to be called
	;from TEXT.003 (item descriptions)
	(method (showSelf)
		(Print INVDESC description
			#title name
			#icon view 0 0
		)
	)
)

;add inventory items here

(instance Test_Object of GameInvItem
	(properties
		name {Test Object}
		said '/object'
		view vTestObject
		loop 0
		cel 0
	)
)
