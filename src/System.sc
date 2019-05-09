;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  SYSTEM.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1988
;;;;
;;;;  Author:  Jeff Stephenson
;;;;  Updated: Brian K. Hughes
;;;;
;;;;  General purpose 'system' classes, presumably not adventure-game
;;;;  specific.  Defines the base class, Object, and all Collection
;;;;  classes.  Probably should be broken up into several modules.
;;;;
;;;;  Classes:
;;;;     Object
;;;;     Code
;;;;     Collection
;;;;     List
;;;;     Set
;;;;     EventHandler
;;;;     Script
;;;;     Event
;;;;     Cursor (not present in SCI01)
;;;;
;;;;  Procedures
;;;;     sign
;;;;     umod
;;;;     Min
;;;;     Max
;;;;     InRect
;;;;     OneOf
;;;;     WordAt
;;;;     Eval	(not present in SCI01)

(script# SYSTEM) ;SYSTEM = 999
(include game.sh)
(use Main)
(use Intrface)

(public
	sign 0
	umod 1
	Min 2
	Max 3
	InRect 4
	OneOf 5
	WordAt 6
)

(procedure (sign x)
   ;
   ; Return +1 if arg is positive, -1 if negative, 0 if 0

   (return 
      (if (< x 0) -1 else (> x 0))
   )
)

(procedure (umod n modN)
   ;
   ; Unsigned mod function

   (-= n (* modN (/ n modN)))
   (if (< n 0) (+= n modN))
   (return n)
)

(procedure (Min nums &tmp otherMin)
   ;
   ; Find parameter that is the smallest

   (if (or  (== argc 1)
            (< nums (= otherMin (Min &rest)))
      )
      nums
   else 
      otherMin
   )
)

(procedure (Max nums &tmp otherMax)
   ;
   ; Find parameter that is the largest

   (if (or  (== argc 1)
            (> nums (= otherMax (Max &rest)))
      )
      nums
   else
      otherMax
   )
)

(procedure (InRect lEdge tEdge rEdge bEdge ptxOrObj pty)
   ;
   ; Determine if a point or object is in a rectangle

   (return 
      (and 
         (<= lEdge (if (< argc 6) (ptxOrObj x?) else ptxOrObj)  rEdge)   
         (<= tEdge (if (< argc 6) (ptxOrObj y?) else pty  )     bEdge)   
      )
   )
)

(procedure (OneOf what things &tmp i)
   ;
   ; Determine if what is one of things

   (for
      (  (= i 0)  )
      (< i (- argc 1))
      (  (++ i)   )
      
      (if (== what [things i]) (return (or what TRUE)))
   )
   (return FALSE)
)

(procedure (WordAt ptr n)
   ;
   ; Return the value of the word at word number (ptr + n)

   (return  (Memory MReadWord (+ ptr (* 2 n))))
)


(class Object
   ;;; Class Object is the superclass of all other Script objects.  It
   ;;; defines the behavior which is expected of all objects, ensuring
   ;;; that all objects will respond to a certain set of selectors.
   
   (properties
      name "Obj"        ;print name of the object
   )
	
	(method (new)
		(Clone self)
	)
	
	(method (init)
	)
	
	(method (doit)
		(return self)
	)
	
	(method (dispose)
		(DisposeClone self)
	)
	
	(method (showStr param1)
		(StrCpy param1 name)
	)
	
	(method (showSelf &tmp [str 200])
		(Print (self showStr: @str))
	)
	
	(method (perform theCode)
		(theCode doit: self &rest)
	)
	
	(method (isKindOf what &tmp theSuper)
			;
			; Are we decended from what?

		(= theSuper (self superClass?))
		(cond 
			((== species (what species?)))
			((IsObject theSuper) (theSuper isKindOf: what))
		)
	)
	
	(method (isMemberOf what)
      ;
      ; Are we an instance of what?

		(return
			(if
				(and
					(& (what -info-?) CLASS)
					(not (& -info- CLASS))
				)
				(== species (what species?))
			else
				FALSE
			)
		)
	)
	
	(method (respondsTo selector)
		(RespondsTo self selector)
	)
	
	(method (yourself)
		(return self)
	)
)

(class Code of Object
   ;;; Class Code is used to attach code to various objects (as in
   ;;; the 'viewer' property of an Actor) or for use in the perform:
   ;;; method of Object.  In either case, it is only the doit: method
   ;;; of the class which is important.

	(method (doit)
	)
)

(class Collection kindof Object
   ;;; The Collection class provides the ability to manipulate
   ;;; collections of objects.  Objects which belong to a Collection
   ;;; are said to be elements or members of it.  The Collection class
   ;;; has no particular order defined for its elements, so it should
   ;;; not be used for situations in which the objects should be
   ;;; ordered -- use a List instead.
   
   (properties
		name	"Collect"
		elements 0
		size 0
	)
	
	(method (dispose)
		(if elements
			(self eachElementDo: #dispose)
			(DisposeList elements)
		)
		(= size (= elements 0))
		(super dispose:)
	)
	
	(method (showStr where)
		(Format where "%s\n[Collection of size %d]" name size)
	)
	
	(method (showSelf &tmp [str 40])
		(Print (self showStr: @str))
		(self eachElementDo: #showSelf)
	)
	
	(method (add item &tmp obj n node)
		;If the Collection does not have a KList, create one for it.
		(if (not elements)
			(= elements (NewList))
		)
		
		;Loop over the arguments, adding each to the Collection.
		(= n 0)
		(while (< n argc)
			(AddToEnd
				elements
				(NewNode [item n] [item n])
			)
			(++ size)
			(++ n)
		)
		(return self)
	)
	
	(method (delete item &tmp n)
		;Loop over arguments, deleting each from the Collection.
		(= n 0)
		(while (< n argc)
			(if (DeleteKey elements [item n])
				(-- size)
			)
			(++ n)
		)
		(return self)
	)
	
	(method (eachElementDo action &tmp node nextNode obj)
		(= node (FirstNode elements))
		(while node
			(= nextNode (NextNode node))
			(if (not (IsObject (= obj (NodeValue node))))
				(return)
			)
			(obj action: &rest)
			(= node nextNode)
		)
	)
	
	(method (firstTrue action &tmp node nextNode obj)
		(= node (FirstNode elements))
		(while node
			(= nextNode (NextNode node))
			(= obj (NodeValue node))
			(if (obj action: &rest) (return obj))
			(= node nextNode)
		)
		(return NULL)
	)
	
	(method (allTrue action &tmp node nextNode obj)
		(= node (FirstNode elements))
		(while node
			(= nextNode (NextNode node))
			(= obj (NodeValue node))
			(if (not (obj action: &rest)) (return FALSE))
			(= node nextNode)
		)
		(return TRUE)
	)
	
	(method (contains anObject)
		(FindKey elements anObject)
	)
	
	(method (isEmpty)
		(if (== elements NULL) else (EmptyList elements))
	)
	
	(method (first)
		(FirstNode elements)
	)
	
	(method (next node)
		(NextNode node)
	)
	
   (method (release &tmp node nextNode)
      ;
      ; Delete all elements from collection to deallocate list nodes

      (for  ((= node (FirstNode elements)))
            node
            ((= node nextNode))
         (= nextNode (NextNode node))
         (self delete: (NodeValue node))
      )
   )
)

(class List of Collection
   ;;; A List is a Collection which has a specified order to its
   ;;; elements.  You will probably use the Set class (below) more
   ;;; than List, as Set automatically weeds out duplicates.

	(properties
		elements 0
		size 0
	)
	
	(method (showStr where)
		(Format where "%s\n[List of size %d]" name size)
	)
	
	(method (at n &tmp node)
		(= node (FirstNode elements))
		(while (and n node)
			(-- n)
			(= node (NextNode node))
		)
		(return (if node (NodeValue node) else 0))
	)
	
	(method (last)
		(LastNode elements)
	)
	
	(method (prev node)
		(PrevNode node)
	)
	
	(method (addToFront obj &tmp n)
		(if (not elements) (= elements (NewList)))
		(= n (- argc 1))
		(while (<= 0 n)
			(AddToFront
				elements
				(NewNode [obj n] [obj n])
			)
			(++ size)
			(-- n)
		)
		(return self)
	)
	
	(method (addToEnd obj &tmp n)
		(if (not elements) (= elements (NewList)))
		(= n 0)
		(while (< n argc)
			(AddToEnd
				elements
				(NewNode [obj n] [obj n])
			)
			(++ size)
			(++ n)
		)
		(return self)
	)
	
	(method (addAfter el obj &tmp n num insertNode)
		(if (= insertNode (FindKey elements el))
			(-- argc)
			(= n 0)
			(while (< n argc)
				(= insertNode
					(AddAfter
						elements
						insertNode
						(NewNode [obj n] [obj n])
					)
				)
				(++ size)
				(++ n)
			)
		)
		(return self)
	)
	
   (method (indexOf obj &tmp n node)
      (for  ((= n 0) (= node (FirstNode elements)))   ;(= node (self first:))
            node
            ((++ n) (= node (NextNode node)))         ;(= node (self next:node))
         (if (== obj (NodeValue node))
            (return n)
         )
      )
      (return -1)
   )
)


(class Set of List
   ;;; A Set is a kind of List which does not contain duplicate
   ;;; elements: adding an object to a Set which already contains the
   ;;; object does not change the Set.  This eliminates the
   ;;; possibility of a whole class of errors based on multiple
   ;;; occurances of an object in a collection.  Most system
   ;;; collections (the cast, etc.) are Sets.

	(properties
		elements 0
		size 0
	)
	
	(method (showStr where)
		(Format where "%s\n[Set of size %d]" name size)
	)
	
	(method (add item &tmp obj n node)
		(if (not elements) (= elements (NewList)))
		(= n 0)
		(while (< n argc)
			(= node [item n])
			(if (not (self contains: node))
				(AddToEnd elements (NewNode node node))
				(++ size)
			)
			(++ n)
		)
	)
)

(class EventHandler of Set
   ;;; An EventHandler is a Set that passes events to its elements.

	(properties
		elements 0
		size 0
	)
	
	(method (handleEvent event &tmp node nextNode obj)
		(= node (FirstNode elements))
		(while (and node (not (event claimed?)))
			(= nextNode (NextNode node))
			(breakif (not (IsObject (= obj (NodeValue node)))))
			(obj handleEvent: event)
			(= node nextNode)
		)
		(event claimed?)
	)
)

(class Script of Object
   ;; A Script is a kind of Object which has a state, methods to
   ;; change that state, and code to execute when the state changes.
   ;; It is used to model a sequence of actions which should be
   ;; executed by an object, such as an Actor walking to the base of
   ;; some stairs, walking up the stairs, and opening a door.

	(properties
		client 0	;the Object whose actions are controlled by this Script
		state $ffff	;the current state of the script
		start 0		;the starting state of the script
		timer 0		;the ID of a timer whose client is this Script
		cycles 0	;the number of cycles to wait before changing state
		seconds 0	;the number of seconds to wait before changing state
		lastSeconds 0	;<private>
		register 0	;open architecture property, can be anything the
                    ;programmer wants it to be, including a list
		script 0	;sub-scripts provide subroutine-style functionality
		caller 0	;who should we cue when we're disposed
		next 0		;who should we CHAIN to when we're disposed
                    ;can be pointer to an instance of Script or
                    ;number of file that has a Script as public zero

	)
	
	(method (init who whoCares reg)
		(if (>= argc 1)
			((= client who) script: self)
			(if (>= argc 2)
				(= caller whoCares)
				(if (>= argc 3) (= register reg))
			)
		)
		(self changeState: start)
	)
	
	(method (doit &tmp thisSeconds)
		(if script (script doit:))
		(cond 
			(cycles (if (not (-- cycles)) (self cue:)))
			(
				(and
					seconds
					(!= lastSeconds (= thisSeconds (GetTime SYSTIME1)))
				)
				(= lastSeconds thisSeconds)
				(if (not (-- seconds)) (self cue:))
			)
		)
	)
	
	(method (dispose &tmp theNextScript)
		(if (IsObject script) (script dispose:))
		(if (IsObject timer) (timer dispose:))
		(if (IsObject client)
			(client
				script:
					(= theNextScript
						(cond 
							((IsObject next)	; script in memory
								next
								)
							(next				; next is module number
								(ScriptID next)
								)
						)
					)
			)
			(cond 
				((not theNextScript)					;no next, nothing to do
					FALSE)
				((== newRoomNum curRoomNum)				;have next, no room change
					(theNextScript init: client))
				(else									;room change
					(theNextScript dispose:)			;clean up clones!

					)
			)
		)
	  ;;NOTE: client's script property MUST be changed BEFORE cue'ing caller
      ;;      We DON'T cue caller on room changes.
      ;;
		(if
		(and (IsObject caller) (== newRoomNum curRoomNum))
			(caller cue: register)	;return register value
		)
	
      ;;the following cleanup statement allow a "disposed" static script 
      ;;to be reused, even though you shouldn't do it! More proper is to 
      ;;Clone a script if it will be used many times.
      ;;

		(= script (= timer (= client (= next (= caller 0)))))
		(super dispose:)
	)
	
	(method (changeState newState)
		(= state newState)
	)
	
	(method (cue)
		(if client	;; Don't changeState if we're an orphan script
			(self changeState: (+ state 1) &rest))
	)
	
	(method (handleEvent event)
		(if script (script handleEvent: event))
		(event claimed?)
	)
	
	(method (setScript newScript)
		(if (IsObject script) (script dispose:))
		(if newScript (newScript init: self &rest)	;init sets our script property!
			)
	)
)

(class Event of Object
   ;;; The Event class is the class of user input events (key presses,
   ;;; mouse clicks, etc.)
	(properties
		type 0			;the type of event (Event types in system.sh)
		message 0		;the value of the event (e.g. ASCII value of key)
		modifiers 0		;modifiers of event (shiftDown, ctrlDown, altDown)
		y 0				;y coord of mouse when event occurred
		x 0				;x coord of mouse when event occurred
		claimed 0		;TRUE if the event has been responded to
	)
	
	(method (new mask &tmp event)
		(= event (super new:))
		(GetEvent (if argc mask else allEvents)
			event
			)
		(return event)
	)
)
