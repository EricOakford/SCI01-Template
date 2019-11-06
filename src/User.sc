;;; Sierra Script 1.0 - (do not remove this comment)
(script# 996)
(include game.sh)
(use Main)
(use Intrface)
(use Sound)
(use SortCopy)
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
	
	(method (handleEvent event &tmp temp0)
		(asm
			pToa     script
			bnt      code_03bc
			pushi    #handleEvent
			pushi    1
			lsp      event
			send     6
code_03bc:
			pushi    #claimed
			pushi    0
			lap      event
			send     4
			not     
			bnt      code_0469
			pushi    #type
			pushi    0
			lap      event
			send     4
			push    
			dup     
			ldi      1
			eq?     
			bnt      code_0424
			pushi    #controls
			pushi    0
			class    User
			send     4
			bnt      code_0468
			pushi    #modifiers
			pushi    0
			lap      event
			send     4
			not     
			bnt      code_0468
			pushi    252
			pushi    3
			lag      useObstacles
			bnt      code_03fb
			class    34
			jmp      code_03fd
code_03fb:
			class    MoveTo
code_03fd:
			push    
			pushi    #x
			pushi    0
			lap      event
			send     4
			push    
			pushi    #y
			pushi    0
			lap      event
			send     4
			push    
			self     10
			pushi    #prevDir
			pushi    1
			pushi    0
			class    User
			send     6
			pushi    #claimed
			pushi    1
			pushi    1
			lap      event
			send     6
			jmp      code_0468
code_0424:
			dup     
			ldi      64
			eq?     
			bnt      code_0468
			pushi    #message
			pushi    0
			lap      event
			send     4
			sat      temp0
			push    
			pushi    #prevDir
			pushi    0
			class    User
			send     4
			eq?     
			bnt      code_044e
			pushi    1
			pTos     mover
			callk    IsObject,  2
			bnt      code_044e
			ldi      0
			sat      temp0
code_044e:
			pushi    #prevDir
			pushi    1
			lst      temp0
			class    User
			send     6
			pushi    #setDirection
			pushi    1
			lst      temp0
			self     6
			pushi    #claimed
			pushi    1
			pushi    1
			lap      event
			send     6
code_0468:
			toss    
code_0469:
			pushi    #claimed
			pushi    0
			lap      event
			send     4
			ret     
		)
	)
	
	(method (get what &tmp i)
		;; Put a number of items into Ego's inventory.
		
		(for	((= i 0))
			(< i argc)
			((++ i))
			
			((inventory at:[what i]) moveTo:self)
		)
	)
	
	(method (put what recipient)
		;; Put an item of Ego's inventory into the inventory of 'recipient'.
		;; If recipient is not present, put item into limbo (-1 owner).
		
		(if (self has:what)
			((inventory at:what) moveTo:(if (== argc 1) -1 else recipient))
		)
	)
	
	(method (has what &tmp theItem)
		;; Return TRUE if Ego has 'what' in inventory.
		
		(= theItem (inventory at:what))
		(return (and theItem (theItem ownedBy:self)))
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
