;;; Sierra Script 1.0 - (do not remove this comment)
(script# 996)
(include game.sh)
(use Main)
(use Intrface)
(use PolyPath)
(use SortCopy)
(use Sound)
(use Motion)
(use Menu)
(use Actor)
(use System)


(local
	[inputLine 23]
	inputLen
)
(instance uEvt of Event
	(properties)
)

(class User of Object
	(properties
		alterEgo 0
		input 0
		controls 0
		echo 32
		prevDir 0
		prompt {Enter input}
		inputLineAddr 0
		x -1
		y -1
		blocks 1
		mapKeyToDir 1
		curEvent 0
		verbMessager 0
	)
	
	(method (init param1 param2)
		(= inputLineAddr (if argc param1 else @inputLine))
		(= inputLen (if (== argc 2) param2 else 45))
		(= curEvent uEvt)
		(if (not verbMessager) (= verbMessager VerbMessager))
	)
	
	(method (doit)
		(if (== 0 demoScripts)
			(curEvent
				type: 0
				message: 0
				modifiers: 0
				y: 0
				x: 0
				claimed: 0
			)
			(GetEvent allEvents curEvent)
			(self handleEvent: curEvent)
		)
	)
	
	(method (canControl theControls)
		(if argc (= controls theControls) (= prevDir 0))
		(return controls)
	)
	
	(method (getInput param1 &tmp temp0 temp1)
		(if (!= (param1 type?) 4) (= inputLine 0))
		(if (!= (param1 message?) echo)
			(Format @inputLine USER 0 (param1 message?))
		)
		(= temp0 (Sound pause: blocks))
		(= temp1 (GetInput @inputLine inputLen prompt 67 x y))
		(Sound pause: temp0)
		(return temp1)
	)
	
	(method (said param1 &tmp temp0)
		(param1
			message: (if verbMessager (verbMessager doit:) else 0)
		)
		(if useSortedFeatures
			(SortedAdd)
		else
			(sortedFeatures add: cast features)
		)
		(if TheMenuBar (sortedFeatures addToFront: TheMenuBar))
		(if firstSaidHandler
			(sortedFeatures addToFront: firstSaidHandler)
		)
		(sortedFeatures
			addToEnd: theGame
			handleEvent: param1
			release:
		)
		(if
		(and (== (param1 type?) saidEvent) (not (param1 claimed?)))
			(theGame pragmaFail: @inputLine)
		)
	)
	
	(method (handleEvent event &tmp eventType temp1)
		(if (event type?)
			(= lastEvent event)
			(= eventType (event type?))
			(if mapKeyToDir (MapKeyToDir event))
			(if TheMenuBar (TheMenuBar handleEvent: event eventType))
			(GlobalToLocal event)
			(if (not (event claimed?))
				(theGame handleEvent: event eventType)
			)
			(if
				(and
					controls
					(not (event claimed?))
					(cast contains: alterEgo)
				)
				(alterEgo handleEvent: event)
			)
			(if
				(and
					input
					(not (event claimed?))
					(== (event type?) keyDown)
					(or
						(== (event message?) echo)
						(and
							(<= SPACEBAR (event message?))
							(<= (event message?) 255)
						)
					)
					(self getInput: event)
					(Parse @inputLine event)
				)
				(event type: saidEvent)
				(self said: event)
			)
		)
		(= lastEvent 0)
	)
	
	(method (canInput theInput)
		(if argc (= input theInput))
		(return input)
	)
)

(class Ego of Actor
	(properties
		signal ignrHrz
		edgeHit 0
	)
	
	(method (init)
		(super init:)
		(if (not cycler) (self setCycle: Walk))
	)
	
	(method (doit)
		(super doit:)
		(= edgeHit
			(cond 
				((<= x 0) 4)
				((>= x 319) 2)
				((>= y 189) 3)
				((<= y (curRoom horizon?)) 1)
				(else 0)
			)
		)
	)
	
	(method (handleEvent event &tmp eventMessage)
		(if script (script handleEvent: event))
		(if (not (event claimed?))
			(switch (event type?)
				(mouseDown
					(if
					(and (User controls?) (not (event modifiers?)))
						(self
							setMotion: (if useObstacles PolyPath else MoveTo) (event x?) (event y?)
						)
						(User prevDir: dirStop)
						(event claimed: TRUE)
					)
				)
				(direction
					(if
						(and
							(== (= eventMessage (event message?)) (User prevDir?))
							(IsObject mover)
						)
						(= eventMessage 0)
					)
					(User prevDir: eventMessage)
					(self setDirection: eventMessage)
					(event claimed: TRUE)
				)
			)
		)
		(event claimed?)
	)
	
	(method (get param1 &tmp temp0)
		(= temp0 0)
		(while (< temp0 argc)
			((inventory at: [param1 temp0]) moveTo: self)
			(++ temp0)
		)
	)
	
	(method (put param1 param2)
		(if (self has: param1)
			((inventory at: param1)
				moveTo: (if (== argc 1) -1 else param2)
			)
		)
	)
	
	(method (has param1 &tmp temp0)
		(if (= temp0 (inventory at: param1))
			(temp0 ownedBy: self)
		)
	)
)

(class VerbMessager of Code
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
				((Said 'get>') verbGet)
				((Said 'climb>') verbClimb)
			)
		)
	)
)
