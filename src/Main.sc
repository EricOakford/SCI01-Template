;;; Sierra Script 1.0 - (do not remove this comment)

;	This is the main game script. It contains the main game class, all the global variables, and
;	a number of useful procedures.

(script# MAIN)
(include system.sh) (include sci2.sh) (include game.sh)
(use Intrface)
(use LoadMany)
(use Motion)
(use StopWalk)
(use Sound)
(use Save)
(use Game)
(use User)
(use Invent)
(use Menu)
(use System)

(public
	SCI01 0 ;Replace "SCI01" with the game's internal name here (up to 6 characters)
	AnimateCast 1	
	HandsOn 2
	HandsOff 3
	cls 4
	Btst 5
	Bset 6
	Bclr 7
	SolvePuzzle 8
	EgoDead	9
	PrintDontHaveIt 10
	PrintAlreadyDoneThat 11
	PrintNotCloseEnough 12
)

(local
	ego				;pointer to ego
	theGame			;ID of the Game instance
	curRoom			;ID of current room
	speed =  6		;current game speed
	quit			;when TRUE, quit game
	cast			;collection of actors
	regions			;set of current regions
	timers			;list of timers in the game
	sounds			;set of sounds being played
	inventory		;set of inventory items in game
	addToPics		;list of views added to the picture
	curRoomNum		;current room number
	prevRoomNum		;previous room number
	newRoomNum		;number of room to change to
	debugOn			;generic debug flag -- set from debug menu
	score			;the player's current score
	possibleScore	;highest possible score
	showStyle =  7	;The global style for the transition from one picture to another.  This
     				;may be overridden by the style property of a given room.  See the
     				;DrawPic kernel function for the possible styles.
	aniInterval			;The number of timer ticks more than the Game's speed which it took to
     				;complete the last animation cycle.  A non-zero aniInterval means that the
     				;system is not keeping up.
	theCursor							;the number of the current cursor
	normalCursor = 		ARROW_CURSOR	;number of normal cursor form
	waitCursor	 = 		HAND_CURSOR		;cursor number of "wait" cursor
	userFont	 = 		USERFONT		;font to use for Print
	smallFont	 = 		4				;small font for save/restore, etc.
	lastEvent							;the last event (used by save/restore game)
	modelessDialog						;the modeless Dialog known to User and Intrface
	bigFont =  USERFONT					;large font
	volume =  12						;current game volume
	version								;pointer to 'incver' version string
                                        ;WARNING!  Must be set in room 0
                                        ;(usually to {x.yyy    } or {x.yyy.zzz})
	locales
	curSaveDir		;address of current save drive/directory string
	;31-49 are unused
	global31
	global32
	global33
	global34
	global35
	global36
	global37
	global38
	global39
	global40
	global41
	global42
	global43
	global44
	global45
	global46
	global47
	global48
	global49
	aniThreshold =  10
	perspective				;player's viewing angle: degrees away from vertical along y axis
	features				;locations that may respond to events
	sortedFeatures
	useSortedFeatures		;enable cast & feature sorting?
	isDemoGame				;enabled if this is a game demo, and not a full game.??
	egoBlindSpot			;actors behind ego within angle 
							;from straight behind. 
							;Default zero is no blind spot
	overlays =  -1
	doMotionCue				;a motion cue has occurred - process it
	systemWindow			;ID of standard system window
	demoDialogTime =  3
	defaultPalette
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
	global99
	isEgoLocked
	deathMusic	= sDeath	;default death music
	musicChannels
	global103
	debugging	;debug mode enabled
	detailLevel		;detail level (0 = low, 1 = mid, 2 = high, 3 = ultra)
	music			;music object, current playing music
	colorCount
	speedCount		;used to test how fast the system is
					;and used in determining detail level. (used in conjunction with detailLevel)
	global109
	SFX				;sound effect being played
	[gameFlags 10]	;each global can have 16 flags. 10 globals * 16 flags = 160 flags. If you need more flags, just increase the array!
)

(procedure (AnimateCast)
	;Used to animate the cast, generally in a room's init() method.
	(Animate (cast elements?) 0)
)

(procedure (HandsOn)
	;Enable ego control
	(= isEgoLocked FALSE)
	(User canControl: TRUE canInput: TRUE)
	(theGame setCursor: normalCursor (HaveMouse))
)

(procedure (HandsOff)
	;Disable ego control
	(= isEgoLocked TRUE)
	(User canControl: FALSE canInput: FALSE)
	(theGame setCursor: waitCursor TRUE)
	(ego setMotion: 0)
)

(procedure (cls)
	;Clear text from the screen
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
	(SFX stop:)
	(music number: deathMusic play:)
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

(instance statusCode of Code
	(properties)
	
	(method (doit strg)
		(Format strg "___Template Game__________________Score: %d of %d" score possibleScore)
	)
)

(instance GlobalMusic of Sound
	(properties
		number 10
	)
)
(instance egoSW of StopWalk
	(properties)
)
(instance Test_Object of InvItem
	(properties
		name {Test Object}
		description {This is a test object.}
		owner 0
		view 800
		loop 0
		cel 0
	)
)

(instance miscMusic of Sound
	(properties
		number 10
		priority 15
	)
)

(instance SCI01 of Game ;Replace "SCI01" with the game's internal name here (up to 6 characters)
	(properties)
	
	(method (init)
		(= debugging TRUE) ;Set to TRUE if you want to enable the debug features.
		(SysWindow color: vBLACK back: vWHITE) ;These colors can be changed to suit your preferences.
		(= colorCount (Graph GDetect))
		(= systemWindow SysWindow)
		(super init:)
		(= musicChannels (DoSound NumVoices))
		(= useSortedFeatures FALSE) ;set to TRUE if you want to use sorted features
		(= ego egoObj)
		(= doVerbCode VerbCode)
		(User alterEgo: ego)
		(= possibleScore 0)	;Set the maximum score here
		(= showStyle 0)
		(TheMenuBar init: draw: hide:)
		(StatusLine code: statusCode disable:) ;hide the status code at startup
		(StopWalk init:)
		(if debugging
			(self setCursor: normalCursor (HaveMouse) 300 170)
		else
			(HandsOff)
			(self setCursor: normalCursor 0 350 200)
		)
		((= music GlobalMusic) number: 10 owner: self init:)
		((= SFX miscMusic) number: 10 owner: self init:)
		(inventory add:
			;Add your inventory items here. Make sure they are in the same order as the item list in GAME.SH.
				Test_Object
		)
		(self newRoom: SPEEDTEST)
	)
	
	(method (doit)
		(super doit:)
	)
	
	(method (startRoom roomNum &tmp [temp0 12])
		(LoadMany FALSE EXTRA QSOUND GROOPER FORCOUNT SIGHT DPATH)
		(ego setCycle: StopWalk vEgoStand)
		(if debugging
			(if
				(and
					(u> (MemoryInfo FreeHeap) (+ 20 (MemoryInfo LargestPtr)))
					(Print "Memory fragmented." #button {Debug} 1)
				)
				(SetDebug)
			)
			(User canInput: TRUE)
		)
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
					((Said 'die') ;This should be commented out in your game; it is only used to test the EgoDead procedure.
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
					(if (event claimed?) (return))
				)
			)
			(if (event claimed?) (return))
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
	)
)