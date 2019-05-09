;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  INVENT.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1992
;;;;
;;;;  Author:  Unkown
;;;;  Updated:
;;;;     Brian K. Hughes
;;;;     July 24, 1992
;;;;
;;;;  Classes:
;;;;     InvItem
;;;;     Inventory
;;;;     InvD (present in up to SCI01)


(script# INVENT)
(include game.sh)
(use Main)
(use Intrface)
(use Save)
(use System)


(local
	numCols
)
(class InvItem of Object
	;;; An InvItem is something which can be owned by an object in an
	;;; adventure game.
	
	(properties
		said 0
		description 0
		owner 0		;who owns this item
		view 0		;picture of the item
		loop 0
		cel 0
		script 0	;a script that can control the item
	)
	
	(method (showSelf)
		(PrintD (if description else name) view loop cel)
	)
	
	(method (saidMe)
		(Said said)
	)
	
	(method (ownedBy whom)
		(return (== owner whom))
	)
	
	(method (moveTo id)
		;** set item's "owner" to passed object ID
		(= owner id)
		(return self)
	)
	
	(method (changeState newState)
		(if script (script changeState: newState))
	)
)

(class Inventory of Set
	;;; This is the set of all inventory items in the game.
	(properties
		name "Inv"
		elements 0
		size 0
		carrying {You are carrying:}
		empty {You are carrying nothing!}
	)
	
	(method (init)
		(= inventory self)
	)
	
	(method (showSelf who)
		(invD text: carrying doit: who)
	)
	
	(method (saidMe)
		(self firstTrue: #saidMe)
	)
	
	(method (ownedBy whom)
		;** Return the first item in inventory which is owned by `whom'
		(self firstTrue: #ownedBy whom)
	)
)

(instance invD of Dialog
	(properties)
	
	(method (init param1 &tmp temp0 temp1 temp2 temp3 newDText inventoryFirst temp6)
		(= temp2 (= temp0 (= temp1 4)))
		(= temp3 0)
		(= inventoryFirst (inventory first:))
		(while inventoryFirst
			(if
			((= temp6 (NodeValue inventoryFirst)) ownedBy: param1)
				(++ temp3)
				(self
					add:
						((= newDText (DText new:))
							value: temp6
							text: (temp6 name?)
							nsLeft: temp0
							nsTop: temp1
							state: 3
							font: smallFont
							setSize:
							yourself:
						)
				)
				(if
				(< temp2 (- (newDText nsRight?) (newDText nsLeft?)))
					(= temp2 (- (newDText nsRight?) (newDText nsLeft?)))
				)
				(if
					(>
						(= temp1
							(+ temp1 (- (newDText nsBottom?) (newDText nsTop?)) 1)
						)
						140
					)
					(= temp1 4)
					(= temp0 (+ temp0 temp2 10))
					(= temp2 0)
				)
			)
			(= inventoryFirst (inventory next: inventoryFirst))
		)
		(if (not temp3) (self dispose:) (return 0))
		(= window SysWindow)
		(self setSize:)
		(= numCols (DButton new:))
		(numCols
			text: {OK}
			setSize:
			moveTo: (- nsRight (+ 4 (numCols nsRight?))) nsBottom
		)
		(numCols
			move: (- (numCols nsLeft?) (numCols nsRight?)) 0
		)
		(self add: numCols setSize: center:)
		(return temp3)
	)
	
	(method (doit param1 &tmp theNumCols)
		(if (not (self init: param1))
			(Print (inventory empty?))
			(return)
		)
		(self open: 4 15)
		(= theNumCols numCols)
		(repeat
			(if
				(or
					(not (= theNumCols (super doit: theNumCols)))
					(== theNumCols -1)
					(== theNumCols numCols)
				)
				(break)
			)
			((theNumCols value?) showSelf:)
		)
		(self dispose:)
	)
	
	(method (handleEvent event &tmp eventMessage eventType)
		(= eventMessage (event message?))
		(switch (= eventType (event type?))
			(4
				(switch eventMessage
					(KEY_UP (= eventMessage 3840))
					(KEY_NUMPAD2
						(= eventMessage 9)
					)
				)
			)
			(64
				(switch eventMessage
					(JOY_UP
						(= eventMessage 3840)
						(= eventType 4)
					)
					(JOY_DOWN
						(= eventMessage 9)
						(= eventType 4)
					)
				)
			)
		)
		(event type: eventType message: eventMessage)
		(super handleEvent: event)
	)
)
