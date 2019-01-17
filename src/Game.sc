;;; Sierra Script 1.0 - (do not remove this comment)
(script# 994)
(include system.sh) (include sci2.sh)
(use Main)
(use Intrface)
(use Sound)
(use Save)
(use Motion)
(use Invent)
(use User)
(use System)


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
			{Please insert your %s disk in drive %s.}
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

(instance sFeatures of EventHandler
	(properties)
	
	(method (delete param1)
		(super delete: param1)
		(if
			(and
				useSortedFeatures
				(param1 isKindOf: Collection)
				(not (OneOf param1 regions theDoits))
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

(instance demons of EventHandler
	(properties
		name "locales"
	)
)

(instance theAddToPics of EventHandler
	(properties
		name "addToPics"
	)
	
	(method (doit)
		(self eachElementDo: #perform aTOC)
		(AddToPic elements)
	)
)

(instance theControls of Controls
	(properties
		name "controls"
	)
)

(instance theTimers of Set
	(properties
		name "timers"
	)
)

(instance aTOC of Code
	(properties)
	
	(method (doit param1 &tmp temp0 temp1)
		(asm
			pushi    #signal
			pushi    0
			lap      param1
			send     4
			push    
			ldi      16384
			or      
			not     
			bnt      code_0226
			pushi    #xStep
			pushi    0
			lag      ego
			send     4
			push    
			pushi    3
			pushi    #view
			pushi    0
			lag      ego
			send     4
			push    
			pushi    2
			pushi    0
			callk    CelWide,  6
			push    
			ldi      2
			div     
			add     
			sat      temp0
			pushi    #yStep
			pushi    0
			lag      ego
			send     4
			push    
			ldi      2
			mul     
			sat      temp1
			pushi    297
			pushi    1
			pushi    102
			pushi    8
			pushi    #brLeft
			pushi    0
			lap      param1
			send     4
			push    
			lat      temp0
			sub     
			push    
			pushi    2
			pushi    1
			pushi    1
			pushi    #y
			pushi    0
			lap      param1
			send     4
			push    
			callk    CoordPri,  2
			push    
			callk    CoordPri,  4
			push    
			lat      temp1
			sub     
			push    
			pushi    #brRight
			pushi    0
			lap      param1
			send     4
			push    
			lat      temp0
			add     
			push    
			pushi    2
			pushi    1
			pushi    1
			pushi    #y
			pushi    0
			lap      param1
			send     4
			push    
			callk    CoordPri,  2
			push    
			callk    CoordPri,  4
			push    
			lat      temp1
			sub     
			push    
			pushi    #brRight
			pushi    0
			lap      param1
			send     4
			push    
			lat      temp0
			add     
			push    
			pushi    #y
			pushi    0
			lap      param1
			send     4
			push    
			lat      temp1
			add     
			push    
			pushi    #brLeft
			pushi    0
			lap      param1
			send     4
			push    
			lat      temp0
			sub     
			push    
			pushi    #y
			pushi    0
			lap      param1
			send     4
			push    
			lat      temp1
			add     
			push    
			pushi    109
			pushi    0
			pushi    #new
			pushi    0
			class    35
			send     4
			send     24
			push    
			lag      curRoom
			send     6
code_0226:
			ret     
		)
	)
)

(class Game of Object
	(properties
		script 0
		parseLang 1
		printLang 1
		subtitleLang 0
	)
	
	(method (init &tmp obj)
		(= obj Motion)
		(= obj Sound)
		(= obj Save)
		((= cast theCast) add:)
		((= features theFeatures) add:)
		((= saidFeatures sFeatures) add:)
		((= sounds theSounds) add:)
		((= regions theRegions) add:)
		((= theDoits demons) add:)
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
		(Animate (cast elements?) 1)
		(if doMotionCue
			(= doMotionCue 0)
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
		(self setCursor: waitCursor 1)
		(self init:)
		(self setCursor: normalCursor (HaveMouse))
		(while (not quit)
			(self doit:)
			(= overRun (Wait speed))
		)
	)
	
	(method (replay)
		(if lastEvent (lastEvent dispose:))
		(saidFeatures release:)
		(if modelessDialog (modelessDialog dispose:))
		(cast eachElementDo: #perform RestoreUpdate)
		(theGame setCursor: waitCursor 1)
		(DrawPic (curRoom curPic?) 100 dpCLEAR defaultPalette)
		(if (!= overlays -1)
			(DrawPic overlays 100 dpNO_CLEAR defaultPalette)
		)
		(if (curRoom controls?) ((curRoom controls?) draw:))
		(addToPics doit:)
		(theGame setCursor: normalCursor (HaveMouse))
		(StatusLine doit:)
		(DoSound sndNOP)
		(Sound pause: 0)
		(while (not quit)
			(self doit:)
			(= overRun (Wait speed))
		)
	)
	
	(method (newRoom newRoomNumber &tmp [temp0 4] temp4 temp5)
		(addToPics dispose:)
		(features eachElementDo: #perform featureDisposeCode release:)
		(cast eachElementDo: #dispose eachElementDo: #delete)
		(timers eachElementDo: #delete)
		(regions eachElementDo: #perform DisposeNonKeptRegion release:)
		(theDoits eachElementDo: #dispose release:)
		(Animate 0)
		(= prevRoomNum curRoomNum)
		(= curRoomNum newRoomNumber)
		(= newRoomNum newRoomNumber)
		(FlushResources newRoomNumber)
		(= temp4 (self setCursor: waitCursor 1))
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
		(if isDemoGame (curRoom setRegions: 975))
	)
	
	(method (restart)
		(if modelessDialog (modelessDialog dispose:))
		(RestartGame)
	)
	
	(method (restore &tmp [temp0 20] temp20 temp21 temp22 theParseLang)
		(= theParseLang parseLang)
		(= parseLang 1)
		(Load rsFONT smallFont)
		(Load rsCURSOR waitCursor)
		(= temp21 (self setCursor: normalCursor))
		(= temp22 (Sound pause: 1))
		(if (PromptForDiskChange 1)
			(if modelessDialog (modelessDialog dispose:))
			(if (!= (= temp20 (Restore doit: &rest)) -1)
				(self setCursor: waitCursor 1)
				(if (CheckSaveGame name temp20 version)
					(RestoreGame name temp20 version)
				else
					(Print {That game was saved under a different interpreter. It cannot be restored.} #font 0 #button {OK} 1)
					(self setCursor: temp21 (HaveMouse))
					(= parseLang theParseLang)
				)
			else
				(= parseLang theParseLang)
			)
			(PromptForDiskChange 0)
		)
		(Sound pause: temp22)
	)
	
	(method (save &tmp [temp0 20] temp20 temp21 temp22 theParseLang)
		(= theParseLang parseLang)
		(= parseLang 1)
		(Load rsFONT smallFont)
		(Load rsCURSOR waitCursor)
		(= temp21 (self setCursor: normalCursor))
		(= temp22 (Sound pause: 1))
		(if (PromptForDiskChange 1)
			(if modelessDialog (modelessDialog dispose:))
			(if (!= (= temp20 (Save doit: @temp0)) -1)
				(= parseLang theParseLang)
				(= temp21 (self setCursor: waitCursor 1))
				(if (not (SaveGame name temp20 @temp0 version))
					(Print {Your save game disk is full. You must either use another disk or save over an existing saved game.} #font 0 #button {OK} 1)
				)
				(self setCursor: temp21 (HaveMouse))
			)
			(PromptForDiskChange 0)
		)
		(Sound pause: temp22)
		(= parseLang theParseLang)
	)
	
	(method (changeScore param1)
		(= score (+ score param1))
		(StatusLine doit:)
	)
	
	(method (handleEvent pEvent)
		(cond 
			(
				(and
					(not (if useSortedFeatures (== (pEvent type?) evSAID)))
					(or
						(regions handleEvent: pEvent)
						(theDoits handleEvent: pEvent)
					)
				)
			)
			(script (script handleEvent: pEvent))
		)
		(pEvent claimed?)
	)
	
	(method (showMem)
		(Printf
			{Free Heap: %u Bytes\nLargest ptr: %u Bytes\nFreeHunk: %u KBytes\nLargest hunk: %u Bytes}
			(MemoryInfo 1)
			(MemoryInfo 0)
			(>> (MemoryInfo 3) $0006)
			(MemoryInfo 2)
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
		(Animate (cast elements?) 0)
		(Wait 0)
		(Animate (cast elements?) 0)
		(while (> (Wait 0) animationDelay)
			(breakif (== (= temp0 (cast firstTrue: #isExtra)) 0))
			(temp0 addToPic:)
			(Animate (cast elements?) 0)
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
	
	(method (wordFail param1 &tmp [temp0 100])
		(Printf {I don't understand "%s".} param1)
		(return 0)
	)
	
	(method (syntaxFail)
		(Print {That doesn't appear to be a proper sentence.})
	)
	
	(method (semanticFail)
		(Print {That sentence doesn't make sense.})
	)
	
	(method (pragmaFail)
		(Print {You've left me responseless.})
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
			(= initialized 1)
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
	
	(method (handleEvent pEvent)
		(if script (script handleEvent: pEvent))
		(pEvent claimed?)
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
		script 0
		number 0
		timer 0
		keep 0
		initialized 0
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
		(= controls controls)
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
	
	(method (handleEvent pEvent)
		(cond 
			((super handleEvent: pEvent))
			(controls (controls handleEvent: pEvent))
		)
		(pEvent claimed?)
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
	
	(method (setFeatures theFeatures &tmp temp0 [temp1 2])
		(= temp0 0)
		(while (< temp0 argc)
			(features add: [theFeatures temp0])
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
			(theDoits add: temp2)
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
			dpCLEAR
			defaultPalette
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
			dpNO_CLEAR
			defaultPalette
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
		(theDoits delete: self)
		(DisposeScript number)
	)
	
	(method (handleEvent pEvent)
		(pEvent claimed?)
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
		(= state 1)
		(self doit:)
	)
	
	(method (disable)
		(= state 0)
		(self doit:)
	)
)

(instance RestoreUpdate of Code
   ;;; Used by replay: to properly deal with members of the cast which were
   ;;; not updating when the game was saved.

   (properties
      name "RU"
   )

   (method (doit obj &tmp sigBits)
      ;; If the object has underBits, it was not updating.  Its underBits
      ;; property is now invalid, so clear it.  Also, set the signal bits
      ;; to draw the object and stopUpd: it.

      (if (obj underBits?)
         (= sigBits (obj signal?))
         (|= sigBits stopUpdOn)
         (&= sigBits (~ notUpd))
         (obj underBits:0, signal:sigBits)
      )
   )
)



(instance DisposeNonKeptRegion of Code
   ;;; Used during room changes to dispose any Regions whose 'keep' property
   ;;; is not TRUE.

   (properties
      name "DNKR"
   )


   (method (doit region)
      (if (not (region keep?))
         (region dispose:)
      )
   )
)



(instance featureDisposeCode   of Code
   ;; Dispose of features and if it's a View send delete to it also
   ;; since it's really the the delete method that gets rid of a View.
   ;; Views will show up in the features list if they have been addToPic'd
   (properties
      name "fDC"
   )

   (method (doit theFeature)
      (if (theFeature respondsTo: #delete:)
         ;; it's kindOf a View, make sure it's not added to 
         ;; addToPics again, and do a complete disposal
         (theFeature 
            signal:(& (theFeature signal?) (~ viewAdded)), 
            dispose:, 
            delete:
         )
      else
         ;; just a feature, dowse it.
         (theFeature dispose:)
      )
   )
)