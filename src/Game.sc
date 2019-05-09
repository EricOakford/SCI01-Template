;;; Sierra Script 1.0 - (do not remove this comment)
(script# 994)
(include game.sh)
(use Main)
(use Intrface)
(use Sound)
(use Save)
(use Motion)
(use Polygon)
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
	(properties
		name "sortedFeatures"
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

(instance addToObstaclesCode of Code
   (properties
      name  "aTOC"
   )

   (method (doit thePV &tmp dX dY)
      (if (not (& (thePV signal?) ignrAct))
         (= dX (+ (ego xStep?) (/ (CelWide (ego view?) facingSouth 0) 2)))
         (= dY (* (ego yStep?) 2))
         (curRoom addObstacle:
            ((Polygon new:)
               init: 
                  ;; left top
                  (- (thePV brLeft?) dX) 
                  (- (CoordPri PTopOfBand (CoordPri (thePV y?))) dY)

                  ;; right top
                  (+ (thePV brRight?) dX) 
                  (- (CoordPri PTopOfBand (CoordPri (thePV y?))) dY)

                  ;; right bottom
                  (+ (thePV brRight?) dX) 
                  (+ (thePV y?) dY)

                  ;; left bottom
                  (- (thePV brLeft?) dX) 
                  (+ (thePV y?) dY),

               yourself:
            )
         )
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
		((= sortedFeatures sFeatures) add:)
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
			(= aniInterval (Wait speed))
		)
	)
	
	(method (replay)
		(if lastEvent (lastEvent dispose:))
		(sortedFeatures release:)
		(if modelessDialog (modelessDialog dispose:))
		(cast eachElementDo: #perform RestoreUpdate)
		(theGame setCursor: waitCursor 1)
		(DrawPic (curRoom curPic?) 100 dpCLEAR currentPalette)
		(if (!= overlays -1)
			(DrawPic overlays 100 dpNO_CLEAR currentPalette)
		)
		(if (curRoom controls?) ((curRoom controls?) draw:))
		(addToPics doit:)
		(theGame setCursor: normalCursor (HaveMouse))
		(StatusLine doit:)
		(DoSound RestoreSound)
		(Sound pause: 0)
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
					(Print "That game was saved under a different interpreter. It cannot be restored." #font SYSFONT #button {OK} 1)
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
					(Print "Your save game disk is full. You must either use another disk or save over an existing saved game." #font SYSFONT #button {OK} 1)
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
		(Animate (cast elements?) 0)
		(Wait 0)
		(Animate (cast elements?) 0)
		(while (> (Wait 0) aniThreshold)
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
	
	(method (wordFail word &tmp [str 100])
		(Printf "I don't understand \"%s\"." word)
		(return FALSE)
	)
	
	(method (syntaxFail)
		(Print "That doesn't appear to be a proper sentence.")
	)
	
	(method (semanticFail)
		(Print "That sentence doesn't make sense.")
	)
	
	(method (pragmaFail)
		(Print "You've left me responseless.")
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
			dpCLEAR
			currentPalette
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
			currentPalette
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