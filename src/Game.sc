;;; Sierra Script 1.0 - (do not remove this comment)
(script# GAME)
(include game.sh)
(use Main)
(use Intrface)
(use Polygon)
(use Sound)
(use Save)
(use Motion)
(use Invent)
(use User)
(use System)


(instance theCast of EventHandler
	(properties
		name "cast"
	)
)

(instance theFeatures of EventHandler
	(properties
		name "features"
	)
)

(instance theSortedFeatures of EventHandler
	(properties
		name "sFeatures"
	)
	
	(method (delete param1)
		(super delete: param1)
		(if
			(and
				useSortedFeatures
				(param1 isKindOf: Collection)
				(not (OneOf param1 regions locales))
			)
			(param1 release: dispose:)
		)
	)
)

(instance theSounds of EventHandler
	(properties
		name "sounds"
	)
)

(instance theRegions of EventHandler
	(properties
		name "regions"
	)
)

(instance theLocales of EventHandler
	(properties
		name "locales"
	)
)

(instance theAddToPics of EventHandler
	(properties
		name "addToPics"
	)
	
	(method (doit)
		(self eachElementDo: #perform addToObstaclesCode)
		(AddToPic elements)
	)
)

(instance roomControls of Controls
	(properties
		name "controls"
	)
)

(instance theTimers of Set
	(properties
		name "timers"
	)
)

(instance addToObstaclesCode of Code
	(properties
		name "aTOC"
	)
	
	(method (doit param1 &tmp temp0 temp1)
		(if (not (| (param1 signal?) $4000))
			(= temp0
				(+ (ego xStep?) (/ (CelWide (ego view?) 2 0) 2))
			)
			(= temp1 (* (ego yStep?) 2))
			(curRoom
				addObstacle:
					((Polygon new:)
						init:
							(- (param1 brLeft?) temp0)
							(- (CoordPri 1 (CoordPri (param1 y?))) temp1)
							(+ (param1 brRight?) temp0)
							(- (CoordPri 1 (CoordPri (param1 y?))) temp1)
							(+ (param1 brRight?) temp0)
							(+ (param1 y?) temp1)
							(- (param1 brLeft?) temp0)
							(+ (param1 y?) temp1)
						yourself:
					)
			)
		)
	)
)

(class Game of Object
	(properties
		script 0
		parseLang ENGLISH
		printLang ENGLISH
		subtitleLang NULL
	)
	
	(method (init &tmp foo)
		(= foo Motion)
		(= foo Sound)
		(= foo Save)
		((= cast theCast) add:)
		((= features theFeatures) add:)
		((= sortedFeatures theSortedFeatures) add:)
		((= sounds theSounds) add:)
		((= regions theRegions) add:)
		((= locales theLocales) add:)
		((= addToPics theAddToPics) add:)
		((= timers theTimers) add:)
		(= curSaveDir (GetSaveDir))
		(Inventory init:)
		(User init:)
	)
	
	(method (doit)
		(sounds eachElementDo: #check)
		(timers eachElementDo: #doit)
		(if modelessDialog (modelessDialog check:))
		(Animate (cast elements?) TRUE)
		(if doMotionCue
			(= doMotionCue FALSE)
			(cast eachElementDo: #motionCue)
		)
		(if script (script doit:))
		(regions eachElementDo: #doit)
		(if (== newRoomNum curRoomNum) (User doit:))
		(if (!= newRoomNum curRoomNum)
			(self newRoom: newRoomNum)
		)
		(timers eachElementDo: #delete)
		(GameIsRestarting 0)
	)
	
	(method (showSelf)
		(regions showSelf:)
	)
	
	(method (play)
		(= theGame self)
		(= curSaveDir (GetSaveDir))
		(if (not (GameIsRestarting)) (GetCWD curSaveDir))
		(self setCursor: waitCursor TRUE)
		(self init:)
		(self setCursor: normalCursor (HaveMouse))
		(while (not quit)
			(self doit:)
			(= aniInterval (Wait speed))
		)
	)
	
	(method (replay)
		(if lastEvent (lastEvent dispose:))
		(sortedFeatures release:)
		(if modelessDialog (modelessDialog dispose:))
		(cast eachElementDo: #perform RestoreUpdate)
		(theGame setCursor: waitCursor TRUE)
		(DrawPic (curRoom curPic?) PLAIN TRUE currentPalette)
		(if (!= overlays -1)
			(DrawPic overlays PLAIN FALSE currentPalette)
		)
		(if (curRoom controls?) ((curRoom controls?) draw:))
		(addToPics doit:)
		(theGame setCursor: normalCursor (HaveMouse))
		(StatusLine doit:)
		(DoSound RestoreSound)
		(Sound pause: FALSE)
		(while (not quit)
			(self doit:)
			(= aniInterval (Wait speed))
		)
	)
	
	(method (newRoom newRoomNumber &tmp [temp0 4] temp4 temp5)
		(addToPics dispose:)
		(features eachElementDo: #perform featureDisposeCode release:)
		(cast eachElementDo: #dispose eachElementDo: #delete)
		(timers eachElementDo: #delete)
		(regions eachElementDo: #perform DisposeNonKeptRegion release:)
		(locales eachElementDo: #dispose release:)
		(Animate 0)
		(= prevRoomNum curRoomNum)
		(= curRoomNum newRoomNumber)
		(= newRoomNum newRoomNumber)
		(FlushResources newRoomNumber)
		(= temp4 (self setCursor: waitCursor TRUE))
		(self
			startRoom: curRoomNum
			checkAni:
			setCursor: temp4 (HaveMouse)
		)
		(SetSynonyms regions)
		(while ((= temp5 (Event new: 3)) type?)
			(temp5 dispose:)
		)
		(temp5 dispose:)
	)
	
	(method (startRoom param1)
		(if debugOn (SetDebug))
		(regions addToFront: (= curRoom (ScriptID param1)))
		(curRoom init:)
		(if demoScripts (curRoom setRegions: DEMO))
	)
	
	(method (restart)
		(if modelessDialog (modelessDialog dispose:))
		(RestartGame)
	)	
	
	(method (save &tmp [comment 20] num oldCur oldPause oldLang)
		(= oldLang parseLang)
		(= parseLang ENGLISH)
		(Load FONT smallFont)
		(Load CURSOR waitCursor)
		(= oldCur (self setCursor: normalCursor))
		(= oldPause (Sound pause: TRUE))
		(if (PromptForDiskChange TRUE)
			(if modelessDialog (modelessDialog dispose:))
			(if (!= (= num (Save doit: @comment)) -1)
				(= parseLang oldLang)
				(= oldCur (self setCursor: waitCursor TRUE))
				(if (not (SaveGame name num @comment version))
					(Print GAME 0
						#font 0
						#button {OK} 1
					)
				)
				(self setCursor: oldCur (HaveMouse))
			)
			(PromptForDiskChange FALSE)
		)
		(Sound pause: oldPause)
		(= parseLang oldLang)
	)
	
	(method (restore &tmp [comment 20] num oldCur oldPause oldLang)
		(= oldLang parseLang)
		(= parseLang ENGLISH)
		(Load FONT smallFont)
		(Load CURSOR waitCursor)
		(= oldCur (self setCursor: normalCursor))
		(= oldPause (Sound pause: TRUE))
		(if (PromptForDiskChange TRUE)
			(if modelessDialog (modelessDialog dispose:))
			(if (!= (= num (Restore doit: &rest)) -1)
				(self setCursor: waitCursor TRUE)
				(if (CheckSaveGame name num version)
					(RestoreGame name num version)
				else
					(Print GAME 1
						#font SYSFONT
						#button {OK} 1
					)
					(self setCursor: oldCur (HaveMouse))
					(= parseLang oldLang)
				)
			else
				(= parseLang oldLang)
			)
			(PromptForDiskChange 0)
		)
		(Sound pause: oldPause)
	)

	(method (changeScore delta)
		(= score (+ score delta))
		(StatusLine doit:)
	)
	
	(method (handleEvent event)
		(cond 
			(
				(and
					(not (if useSortedFeatures (== (event type?) saidEvent)))
					(or
						(regions handleEvent: event)
						(locales handleEvent: event)
					)
				)
			)
			(script (script handleEvent: event))
		)
		(event claimed?)
	)
	
	(method (showMem)
		(Printf
			{Free Heap: %u Bytes\nLargest ptr: %u Bytes\nFreeHunk: %u KBytes\nLargest hunk: %u Bytes}
			(MemoryInfo FreeHeap)
			(MemoryInfo LargestPtr)
			(>> (MemoryInfo FreeHunk) $0006)
			(MemoryInfo LargestHandle)
		)
	)
	
	(method (setSpeed newSpeed &tmp theSpeed)
		(= theSpeed speed)
		(= speed newSpeed)
		(return theSpeed)
	)
	
	(method (setCursor cursorNumber &tmp theTheCursor)
		(= theTheCursor theCursor)
		(= theCursor cursorNumber)
		(SetCursor cursorNumber &rest)
		(return theTheCursor)
	)
	
	(method (checkAni &tmp temp0)
		(Animate (cast elements?) FALSE)
		(Wait 0)
		(Animate (cast elements?) FALSE)
		(while (> (Wait 0) aniThreshold)
			(breakif (== (= temp0 (cast firstTrue: #isExtra)) 0))
			(temp0 addToPic:)
			(Animate (cast elements?) FALSE)
			(cast eachElementDo: #delete)
		)
	)
	
	(method (notify)
	)
	
	(method (setScript theScript)
		(if script (script dispose:))
		(if theScript (theScript init: self &rest))
	)
	
	(method (cue)
		(if script (script cue:))
	)
	
	(method (wordFail word &tmp [str 100])
		(Printf GAME 2 word)
		(return FALSE)
	)
	
	(method (syntaxFail)
		(Print GAME 3)
	)
	
	(method (semanticFail)
		(Print GAME 4)
	)
	
	(method (pragmaFail)
		(Print GAME 5)
	)
)

(class Region of Object
	(properties
		name "Rgn"
		script 0
		number 0
		timer 0
		keep 0
		initialized 0
	)
	
	(method (init)
		(if (not initialized)
			(= initialized TRUE)
			(if (not (regions contains: self))
				(regions addToEnd: self)
			)
			(super init:)
		)
	)
	
	(method (doit)
		(if script (script doit:))
	)
	
	(method (dispose)
		(regions delete: self)
		(if (IsObject script) (script dispose:))
		(if (IsObject timer) (timer dispose:))
		(sounds eachElementDo: #clean self)
		(DisposeScript number)
	)
	
	(method (handleEvent event)
		(if script (script handleEvent: event))
		(event claimed?)
	)
	
	(method (setScript theScript)
		(if (IsObject script) (script dispose:))
		(if theScript (theScript init: self &rest))
	)
	
	(method (cue)
		(if script (script cue:))
	)
	
	(method (newRoom)
	)
	
	(method (notify)
	)
)

(class Room of Region
	(properties
		name "Rm"
		picture 0
		style $ffff
		horizon 0
		controls 0
		north 0
		east 0
		south 0
		west 0
		curPic 0
		picAngle 0
		vanishingX 160
		vanishingY -30000
		obstacles 0
	)
	
	(method (init &tmp temp0)
		(= number curRoomNum)
		(= controls roomControls)
		(= perspective picAngle)
		(if picture (self drawPic: picture))
		(switch ((User alterEgo?) edgeHit?)
			(1 ((User alterEgo?) y: 188))
			(4
				((User alterEgo?)
					x: (- 319 ((User alterEgo?) xStep?))
				)
			)
			(3
				((User alterEgo?)
					y: (+ horizon ((User alterEgo?) yStep?))
				)
			)
			(2 ((User alterEgo?) x: 1))
		)
		((User alterEgo?) edgeHit: 0)
	)
	
	(method (doit &tmp temp0)
		(if script (script doit:))
		(if
			(= temp0
				(switch ((User alterEgo?) edgeHit?)
					(1 north)
					(2 east)
					(3 south)
					(4 west)
				)
			)
			(self newRoom: temp0)
		)
	)
	
	(method (dispose)
		(if controls (controls dispose:))
		(if obstacles (obstacles dispose:))
		(super dispose:)
	)
	
	(method (handleEvent event)
		(cond 
			((super handleEvent: event))
			(controls (controls handleEvent: event))
		)
		(event claimed?)
	)
	
	(method (newRoom newRoomNumber)
		(regions
			delete: self
			eachElementDo: #newRoom newRoomNumber
			addToFront: self
		)
		(= newRoomNum newRoomNumber)
		(super newRoom: newRoomNumber)
	)
	
	(method (setRegions scriptNumbers &tmp temp0 theScriptNumbers temp2)
		(= temp0 0)
		(while (< temp0 argc)
			(= theScriptNumbers [scriptNumbers temp0])
			((= temp2 (ScriptID theScriptNumbers))
				number: theScriptNumbers
			)
			(regions add: temp2)
			(if (not (temp2 initialized?)) (temp2 init:))
			(++ temp0)
		)
	)
	
	(method (setFeatures feature &tmp temp0 [temp1 2])
		(= temp0 0)
		(while (< temp0 argc)
			(features add: [feature temp0])
			(++ temp0)
		)
	)
	
	(method (setLocales scriptNumbers &tmp temp0 theScriptNumbers temp2)
		(= temp0 0)
		(while (< temp0 argc)
			(= theScriptNumbers [scriptNumbers temp0])
			((= temp2 (ScriptID theScriptNumbers))
				number: theScriptNumbers
			)
			(locales add: temp2)
			(temp2 init:)
			(++ temp0)
		)
	)
	
	(method (drawPic picNumber picAnimation)
		(if addToPics (addToPics dispose:))
		(= curPic picNumber)
		(= overlays -1)
		(DrawPic
			picNumber
			(cond 
				((== argc 2) picAnimation)
				((!= style -1) style)
				(else showStyle)
			)
			TRUE currentPalette
		)
	)
	
	(method (overlay picNumber picAnimation)
		(= overlays picNumber)
		(DrawPic
			picNumber
			(cond 
				((== argc 2) picAnimation)
				((!= style -1) style)
				(else showStyle)
			)
			FALSE currentPalette
		)
	)
	
	(method (addObstacle param1)
		(if (not obstacles) (= obstacles (List new:)))
		(obstacles add: param1 &rest)
	)
)

(class Locale of Object
	(properties
		number 0
	)
	
	(method (dispose)
		(locales delete: self)
		(DisposeScript number)
	)
	
	(method (handleEvent event)
		(event claimed?)
	)
)

(class StatusLine of Object
	(properties
		name "SL"
		state $0000
		code 0
	)
	
	(method (doit &tmp [temp0 41])
		(if code
			(code doit: @temp0)
			(DrawStatus (if state @temp0 else 0))
		)
	)
	
	(method (enable)
		(= state TRUE)
		(self doit:)
	)
	
	(method (disable)
		(= state FALSE)
		(self doit:)
	)
)

(instance RestoreUpdate of Code
	(properties
		name "RU"
	)
	
	(method (doit param1 &tmp temp0)
		(if (param1 underBits?)
			(= temp0
				(&
					(= temp0 (| (= temp0 (param1 signal?)) $0001))
					$fffb
				)
			)
			(param1 underBits: 0 signal: temp0)
		)
	)
)

(instance DisposeNonKeptRegion of Code
	(properties
		name "DNKR"
	)
	
	(method (doit param1)
		(if (not (param1 keep?)) (param1 dispose:))
	)
)

(instance featureDisposeCode of Code
	(properties
		name "fDC"
	)
	
	(method (doit param1)
		(if (param1 respondsTo: #delete)
			(param1
				signal: (& (param1 signal?) $ffdf)
				dispose:
				delete:
			)
		else
			(param1 dispose:)
		)
	)
)

(procedure (PromptForDiskChange param1 &tmp temp0 [temp1 40] [temp41 40] [temp81 40])
	(= temp0 1)
	(DeviceInfo 0 curSaveDir @temp1)
	(DeviceInfo 1 @temp41)
	(if
		(and
			(DeviceInfo 2 @temp1 @temp41)
			(DeviceInfo 3 @temp41)
		)
		(Format
			@temp81
			GAME 6
			(if param1 {SAVE GAME} else {GAME})
			@temp41
		)
		(DeviceInfo 4)
		(if
			(==
				(= temp0
					(if param1
						(Print
							@temp81
							#font
							0
							#button
							{OK}
							1
							#button
							{Cancel}
							0
							#button
							{Change Directory}
							2
						)
					else
						(Print @temp81 #font 0 #button {OK} 1)
					)
				)
				2
			)
			(= temp0 (GetDirectory curSaveDir))
		)
	)
	(return temp0)
)