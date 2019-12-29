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
	EgoDead	10
	DontHave 11
	AlreadyDone 12
	NotClose 13
	CantDo 14
	CantSee 15
	InitAddToPics 16
	InitFeatures 17
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
	numColors
	numVoices
	debugging		;debug mode enabled
	howFast			;machine speed level (0 = slow, 1 = medium, 2 = fast, 3 = fastest)
	machineSpeed	;used to test how fast the system is
					; and used in determining game speed. (used in conjunction with howFast)
	theMusic		;music object, current playing music
	soundFx			;sound effect being played
	cIcon			;global pointer to cycling icon
	[gameFlags 10]	;each global can have 16 flags. 10 globals * 16 flags = 160 flags.
					; If you need more flags, just increase the array!
	myTextColor		;color of text in message boxes
	myBackColor		;color of message boxes
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
		setPri: -1
		setMotion: 0
		setCycle: StopWalk vEgoStand
		illegalBits: cWHITE
		cycleSpeed: 0
		moveSpeed: 0
		setStep: 3 2
		ignoreActors: FALSE
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
				(= quit TRUE) (break)
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
;They were used in QFG2, which uses sorted features.
(procedure (InitAddToPics)
	(addToPics add: &rest eachElementDo: #init doit:)
)

(procedure (InitFeatures)
	(features add: &rest eachElementDo: #init doit:)
)


(instance egoObj of Ego
	(properties
		name "ego"
	)
)

(instance statusCode of Code
	(properties)
	
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

(instance deathIcon of DCIcon
	(properties)
)

;	GameVerbMessager can be customized for additional verbs
;	that are not part of the stock USER.SC. This was done in Quest for Glory II.

(instance verbWords of VerbMessager
	(properties)
	
	(method (doit)
		(return
			(cond 
				((Said 'look>') verbLook)
				((Said 'open>') verbOpen)
				((Said 'close>') verbClose)
				((Said 'smell>') verbSmell)
				((Said 'move>') verbMove)
				((Said 'eat>') verbEat)
				((Said 'get,(pick<up)>') verbGet)
				((Said 'climb>') verbClimb)
				((Said 'talk>') verbTalk)
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

(instance SCI01 of Game ;Replace "SCI01" with the game's internal name here (up to 6 characters)
	; The main game instance. It adds game-specific functionality.
	(properties
		;Set your game's language here.
		;Supported langauges can be found in SYSTEM.SH.		
		parseLang ENGLISH
		printLang ENGLISH
	)
	
	(method (init)
		;load some important modules
		Cycle
		StopWalk
		Window
		DCIcon
		TheMenuBar
		;set up various aspects of the game
		(super init:)
		(= cIcon deathIcon)
		(= ego egoObj)
		(= version {x.yyy.zzz}) ;set game version here
		(= doVerbCode DoVerbCode)
		(User alterEgo: ego verbMessager: verbWords)
		(TheMenuBar init: draw: hide: state: FALSE)
		(StatusLine code: statusCode disable:) ;hide the status line at startup
		(if debugging
			(self setCursor: normalCursor (HaveMouse) 300 170)
		else
			(HandsOff)
			(self setCursor: normalCursor FALSE 350 200)
		)
		((= theMusic music) number: sDeath owner: self init:)
		((= soundFx SFX) number: sDeath owner: self init:)
		(inventory add:
			;Add your inventory items here. Make sure they are in the same order as the item list in GAME.SH.
			Test_Object
		)
		;moved any code not requiring any objects in this script into its own script
		((ScriptID GAME_INIT 0) init:)
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
		(NormalEgo)
		(super startRoom: roomNum)
	)
	
	(method (handleEvent event &tmp item)
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
			(super handleEvent: event)
			(if (event claimed?)
				(return)
			)
			(switch (event type?)
				(keyDown
					((ScriptID DEBUG) handleEvent: event)
				)
				(mouseDown
					((ScriptID DEBUG) handleEvent: event)
				)
			)
		else
			(super handleEvent: event)
		)
		(switch (event type?)
		;Add global parser commands here.
			(saidEvent
				(cond
					((Said 'cheat')
						(Print "Okay, you win.")
						(Print "(Game over.)" #at -1 152)
						(= quit TRUE)
					)
					;interactions with inventory items
					(
						(and
							(Said '/*>')
							(= item (inventory saidMe:))
						)
						(event claimed: FALSE)
						(cond 
							((item ownedBy: ego)
								(cond 
									((Said 'look[<at]')
										(item showSelf:)
									)
								)
							)
							((item ownedBy: curRoomNum)
								(if (Said 'get')
									(CantDo)
								else
									(DontHave)
								)
							)
							(else
								(CantSee)
							)
						)
						(event claimed: TRUE)
					)
				)
			)
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
		owner 0
		view vTestObject
		loop 0
		cel 0
	)
)
