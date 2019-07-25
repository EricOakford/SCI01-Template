;;; Sierra Script 1.0 - (do not remove this comment)
(script# 255)
(include game.sh)
(use Main)
(use System)

(public
	Print 0
	ShowView 1
	GetInput 2
	GetNumber 3
	Printf 4
	MousedOn 5
)

(define BMOD 16) ; width equalizer for buttons

(procedure (Print param1 &tmp newDialog newDText newDIcon newDEdit temp4 temp5 temp6 temp7 temp8 theModelessDialog temp10 temp11 [newDButton 6] temp18 temp19 temp20 [temp21 1000])
	(= temp6 (= temp7 -1))
	(= theModelessDialog
		(= temp8
			(= temp18 (= newDIcon (= newDEdit (= temp19 0))))
		)
	)
	((= newDialog (Dialog new:))
		window: systemWindow
		name: {PrintD}
	)
	(= newDText (DText new:))
	(cond 
		((u< [param1 0] 1000) (GetFarText [param1 0] [param1 1] @temp21) (= temp5 2))
		([param1 0] (StrCpy @temp21 [param1 0]) (= temp5 1))
		(else (= temp21 0) (= temp5 0))
	)
	(newDText
		text: @temp21
		moveTo: 4 4
		font: userFont
		setSize:
	)
	(newDialog add: newDText)
	(= temp5 temp5)
	(while (< temp5 argc)
		(switch [param1 temp5]
			(30
				(++ temp5)
				(newDText mode: [param1 temp5])
			)
			(33
				(++ temp5)
				(newDText font: [param1 temp5] setSize: temp8)
			)
			(70
				(= temp8 [param1 (++ temp5)])
				(newDText setSize: temp8)
			)
			(25
				(++ temp5)
				(newDialog time: [param1 temp5])
			)
			(80
				(++ temp5)
				(newDialog text: [param1 temp5])
			)
			(67
				(= temp6 [param1 (++ temp5)])
				(= temp7 [param1 (++ temp5)])
			)
			(83
				(Animate (cast elements?) 0)
			)
			(41
				(++ temp5)
				((= newDEdit (DEdit new:)) text: [param1 temp5])
				(++ temp5)
				(newDEdit max: [param1 temp5] setSize:)
			)
			(81
				((= [newDButton temp19] (DButton new:))
					text: [param1 (++ temp5)]
					value: [param1 (++ temp5)]
					setSize:
				)
				(= temp18 (+ temp18 ([newDButton temp19] nsRight?) 4))
				(++ temp19)
			)
			(82
				(if (IsObject [param1 (+ temp5 1)])
					((= newDIcon ([param1 (+ temp5 1)] new:)) setSize:)
					(= temp5 (+ temp5 1))
				else
					(= newDIcon (DIcon new:))
					(newDIcon
						view: [param1 (+ temp5 1)]
						loop: [param1 (+ temp5 2)]
						cel: [param1 (+ temp5 3)]
						setSize:
					)
					(= temp5 (+ temp5 3))
				)
			)
			(103
				(if
					(and
						(< (+ temp5 1) argc)
						(IsObject [param1 (+ temp5 1)])
					)
					(newDialog caller: [param1 (+ temp5 1)])
					(++ temp5)
				)
				(if modelessDialog (modelessDialog dispose:))
				(= theModelessDialog newDialog)
			)
			(35
				(++ temp5)
				(newDialog window: [param1 temp5])
			)
		)
		(++ temp5)
	)
	(if newDIcon
		(newDIcon moveTo: 4 4)
		(newDText
			moveTo: (+ 4 (newDIcon nsRight?)) (newDText nsTop?)
		)
		(newDialog add: newDIcon)
	)
	(newDialog setSize:)
	(if newDEdit
		(newDEdit
			moveTo: (newDText nsLeft?) (+ 4 (newDText nsBottom?))
		)
		(newDialog add: newDEdit setSize:)
	)
	(= temp20
		(if (> temp18 (newDialog nsRight?))
			4
		else
			(- (newDialog nsRight?) temp18)
		)
	)
	(= temp5 0)
	(while (< temp5 temp19)
		([newDButton temp5] moveTo: temp20 (newDialog nsBottom?))
		(newDialog add: [newDButton temp5])
		(= temp20 (+ 4 ([newDButton temp5] nsRight?)))
		(++ temp5)
	)
	(newDialog setSize: center:)
	(if (and newDIcon (not (StrLen @temp21)))
		(newDIcon
			moveTo:
				(/
					(-
						(- (newDialog nsRight?) (newDialog nsLeft?))
						(- (newDIcon nsRight?) (newDIcon nsLeft?))
					)
					2
				)
				4
		)
	)
	(newDialog
		moveTo:
			(if (== -1 temp6) (newDialog nsLeft?) else temp6)
			(if (== -1 temp7) (newDialog nsTop?) else temp7)
	)
	(= temp11 (GetPort))
	(newDialog
		open: (if (newDialog text?) 4 else 0) (if theModelessDialog 15 else -1)
	)
	(if theModelessDialog
		(= modelessPort (GetPort))
		(SetPort temp11)
		(return (= modelessDialog theModelessDialog))
	)
	(if
		(and
			(= temp10 (newDialog firstTrue: #checkState 1))
			(not (newDialog firstTrue: #checkState 2))
		)
		(temp10 state: (| (temp10 state?) $0002))
	)
	(if (== (= temp4 (newDialog doit: temp10)) -1)
		(= temp4 0)
	)
	(= temp5 0)
	(while (< temp5 temp19)
		(if (== temp4 [newDButton temp5])
			(= temp4 (temp4 value?))
			(break)
		)
		(++ temp5)
	)
	(if (not (newDialog theItem?)) (= temp4 1))
	(newDialog dispose:)
	(return temp4)
)

(procedure (ShowView str view loop cel)
	(Print str #icon view loop cel &rest)
)

(procedure (GetInput inputStr maxLen str &tmp [temp0 4])
	(if
		(Print
			(if (>= argc 3) str else {})
			#edit
			inputStr
			maxLen
			&rest
		)
		(StrLen inputStr)
	)
)


(procedure (GetNumber str default &tmp [theLine 40])
	(= theLine 0)
	(if (> argc 1) (Format @theLine INTRFACE 0 default))
	(return
		(if (GetInput @theLine 5 str)
			(ReadNumber @theLine)
		else
			-1
		)
	)
)

(procedure (Printf &tmp [str 500])
	(Format @str &rest)
	(Print @str)
)


(procedure (MousedOn param1 param2 param3)
	(cond 
		(
			(or
				(!= (param2 type?) 1)
				(and
					(param1 respondsTo: #signal)
					(& (param1 signal?) $0080)
				)
			)
			0
		)
		(
			(and
				(>= argc 3)
				param3
				(== (& (param2 modifiers?) param3) 0)
			)
			0
		)
		((param1 respondsTo: #nsLeft)
			(InRect
				(param1 nsLeft?)
				(param1 nsTop?)
				(param1 nsRight?)
				(param1 nsBottom?)
				param2
			)
		)
	)
)

(procedure (StillDown &tmp evt ret)
	(= ret (!= ((= evt (Event new:)) type?) mouseUp))
	(evt dispose:)
	(return ret)
)


(class MenuBar of Object
	(properties
		name 	NULL
		state 	$0000
	)
	
	(method (draw)
		(= state TRUE)
		(DrawMenuBar TRUE)
	)
	
	(method (hide)
		(= state FALSE)
		(DrawMenuBar FALSE)
	)
	
	(method (handleEvent event &tmp ret oldJoy)
		(= ret 0)
		(if state
			(= oldJoy (Joystick 12 30))
			(= ret (MenuSelect event &rest))
			(Joystick 12 oldJoy)
		)
		(return ret)
	)

	
	(method (add)
		(AddMenu &rest)
	)
)

(class DItem of Object
;;; The superclass of all items of control in the user interface.
	
	(properties
		name 	 NULL	; don't waste storage on a name string
		type 	 $0000	; the type of this control
		state 	 $0000	; defined by each subclass
		nsTop 	 0		; visible rectangle
		nsLeft 	 0		; in LOCAL coords
		nsBottom 0		; used to select
		nsRight  0		; control via a mouse click
		key 	 0		; key code associated with control
		said 	 0		; said spec associated with control
		value 	 0		; for programmers use
	)

	
	(method (doit)
		;; Default method is to return value.
		;; Will be superceded by user's instances.

		(return value)
	)
	
	(method (enable bool)
		;; Enable/disable this control.
		
		(if bool
			(= state (| state dActive))
		else
			(= state (& state (~ dActive)))
		)
	)
	
	(method (select bool)
		;; Select/deselect this control.

		(if bool
			(= state (| state dSelected))
		else
			(= state (& state (~ dSelected)))
		)
		(self draw:)
	)

	
	(method (handleEvent event &tmp ret evtType evt)
		;; Return ID if this event is yours, else 0.
		
		(if (event claimed?) (return NULL))
		
		; default to not selected
		(= ret NULL)
		(if
			(and
				(& state dActive)
				(or
					;something was 'said'
					(and
						; assign to evtType variable for a slight speed up effort
						(== (= evtType (event type?)) saidEvent)
						(Said said)
					)
					; pressed your key
					(and
						(== evtType keyDown)
						(== (event message?) key)
					)
					; clicked in box
					(and (== evtType mouseDown) (self check: event))
				)
			)
			
			; this was us
			(event claimed: TRUE)
			(= ret (self track: event))
		)
		(return ret)
	)

	
	(method (check event)
		;; Return true if x/y/ in your rectangle.
		
		(return
			(if
				(and
					(>= (event x?) nsLeft)
					(>= (event y?) nsTop)
					(< (event x?) nsRight)
				)
				(< (event y?) nsBottom)
			else
				0
			)
		)
	)

	
	(method (track event &tmp in lastIn)
		;; Track control to confirm selection.
		;; NOTE: Only a mouseDown requires a mouse track.

		(return
			(if (== mouseDown (event type?))
				(= lastIn 0)
				(repeat
					(= event (Event new: leaveIt))
					(GlobalToLocal event)
					(if (!= (= in (self check: event)) lastIn)
						(HiliteControl self)
						(= lastIn in)
					)
					(event dispose:)
					(breakif (not (StillDown)))
				)
				(if in (HiliteControl self))
				(return in)
			else
				(return self)
			)
		)
	)

	
	(method (setSize)
		;; Set the item's rectangle.  Responsibility of subclasses.
	)
	
	(method (move h v)
		;; Move item BY h v.
		
		(= nsRight (+ nsRight h))
		(= nsLeft (+ nsLeft h))
		(= nsTop (+ nsTop v))
		(= nsBottom (+ nsBottom v))
	)
	
	(method (moveTo h v)
		;; Move item TO h v.
		
		(self move: (- h nsLeft) (- v nsTop))
	)
	
	(method (draw)
		;; Draw self per kernel definition.
		
		(DrawControl self)
	)

	
	(method (isType theType)
		;; Return TRUE if this DItem is of type theType.
		
		(return (== type theType))
	)
	
	(method (checkState bit)
		(return (& state bit))
	)
	
	(method (cycle)
		; do something on each cycle through the dialog's doit
	)
)


(class DText of DItem
	;;; A non-editable, generally non-selectable text field.
	
	(properties
		;properties from DItem
		type 		dText	;value updated for DText
		state 		$0000
		nsTop 		0
		nsLeft 		0
		nsBottom 	0
		nsRight 	0
		key 		0
		said 		0
		value 		0
		;new properties for DText
		text 		0			;the text in the field
		font 		USERFONT	;font to use for print text
		mode 		teJustLeft	;possible alignment of text
								;  (0) teJustLeft     left justified
								;  (1) teJustCenter   center each line
								;  (-1) teJustRight    right justified
	)

	
	(method (new &tmp newText)
		((super new:) font: userFont yourself:)
	)
	
	(method (setSize w &tmp [r 4])
		;; If w arg is present it is the fixed width of the text rectangle.
		
		(TextSize @[r 0] text font (if argc w else 0))
		(= nsBottom (+ nsTop [r 2]))
		(= nsRight (+ nsLeft [r 3]))
	)
)

(class DIcon of DItem
	;;; Icons are simply a view/loop/cel combination created by the view
	;;; editor VE.  They are generally not selectable.

	(properties
		;properties from DItem
		type 		dIcon ;value updated for DIcon
		state 		$0000
		nsTop 		0
		nsLeft 		0
		nsBottom 	0
		nsRight 	0
		key 		0
		said 		0
		value 		0
		;new properties for DIcon
		view 		NULL	; view number
		loop 		NULL	; loop number
		cel 		NULL	; cel number
	)
	
	(method (setSize &tmp [r 4])
		(= nsRight (+ nsLeft (CelWide view loop cel)))
		(= nsBottom (+ nsTop (CelHigh view loop cel)))
	)
)


(class DButton of DItem
	;;; Buttons are selectable items which a user clicks in with the mouse
	;;; or selects with the TAB key and ENTER in order to execute an action.

	(properties
		;properties from DIcon
		type 		dButton ;value updated for DButton
		state 		(| dActive dExit) ;value updated for DButton
		nsTop 		0
		nsLeft 		0
		nsBottom	0
		nsRight 	0
		key 		0
		said 		0
		value 		0
		;new properties for DButton
		text 		0		;text displayed inside button
		font 		SYSFONT	;should usally be left as the system font
	)
	
	(method (setSize &tmp [r 4])
		(TextSize @[r 0] text font)
		
		; a button box is one pixel larger all around
		(= [r 2] (+ [r 2] 2))
		(= [r 3] (+ [r 3] 2))
		(= nsBottom (+ nsTop [r 2]))
		(= [r 3] (* (/ (+ [r 3] (- BMOD 1)) BMOD) BMOD))
		(= nsRight (+ [r 3] nsLeft))
	)
)

(class DEdit of DItem
	;;; A text field which is editable by the user.
	
	(properties
		;properties from DItem
		type 		dEdit 	;value updated for DEdit
		state 		dActive	;value updated for DEdit
		nsTop 		0
		nsLeft 		0
		nsBottom 	0
		nsRight 	0
		key 		0
		said 		0
		value 		0
		;new properties for DEdit
		text 		0		;default text when the edit item is drawn
		font 		SYSFONT	;this is often changed to a user font
		max 		0		;maximum number of characters allowed in field
		cursor 		0		;cursor position in field
	)
	
	(method (track evt)
		(EditControl self evt)
		(return self)	;used to return 0, see Corey
	)

	
	(method (setSize &tmp [r 4])
		;; Size and set cursor position to the end of the text.
		
		; box is as sized by max * width of an "M"
		(TextSize @[r 0] {M} font)
		(= nsBottom (+ nsTop [r 2]))
		(= nsRight (+ nsLeft (/ (* [r 3] max 3) 4)))
		(= cursor (StrLen text))
	)
)


(class DSelector of DItem
	(properties
		type $0006
		state $0000
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		key 0
		said 0
		value 0
		font 0
		x 20
		y 6
		text 0
		cursor 0
		lsTop 0
		mark 0
	)
	
	(method (handleEvent event &tmp temp0 [temp1 3] temp4 [temp5 4])
		(if (event claimed?) (return 0))
		(if (== evJOYSTICK (event type?))
			(event type: 4)
			(switch (event message?)
				(JOY_DOWN
					(event message: 20480)
				)
				(JOY_UP (event message: 18432))
				(else  (event type: 64))
			)
		)
		(= temp0 0)
		(switch (event type?)
			(keyDown
				(event claimed: 1)
				(switch (event message?)
					(KEY_NUMPAD7 (self retreat: 50))
					(KEY_NUMPAD1 (self advance: 50))
					(KEY_PAGEUP
						(self advance: (- y 1))
					)
					(KEY_PAGEDOWN
						(self retreat: (- y 1))
					)
					(KEY_NUMPAD2 (self advance: 1))
					(KEY_UP (self retreat: 1))
					(else  (event claimed: 0))
				)
			)
			(mouseDown
				(if (self check: event)
					(event claimed: 1)
					(cond 
						((< (event y?) (+ nsTop 10))
							(repeat
								(self retreat: 1)
								(breakif (not (StillDown)))
							)
						)
						((> (event y?) (- nsBottom 10))
							(repeat
								(self advance: 1)
								(breakif (not (StillDown)))
							)
						)
						(else
							(TextSize @[temp5 0] {M} font)
							(if
								(>
									(= temp4 (/ (- (event y?) (+ nsTop 10)) [temp5 2]))
									mark
								)
								(self advance: (- temp4 mark))
							else
								(self retreat: (- mark temp4))
							)
						)
					)
				)
			)
		)
		(return
			(if (and (event claimed?) (& state $0002))
				self
			else
				0
			)
		)
	)
	
	(method (setSize &tmp [temp0 4])
		(TextSize @[temp0 0] {M} font)
		(= nsBottom (+ nsTop 20 (* [temp0 2] y)))
		(= nsRight (+ nsLeft (/ (* [temp0 3] x 3) 4)))
		(= lsTop (= cursor text))
		(= mark 0)
	)
	
	(method (indexOf param1 &tmp theText temp1)
		(= theText text)
		(= temp1 0)
		(return
			(while (< temp1 300)
				(if (== 0 (StrLen theText)) (return -1))
				(if (not (StrCmp param1 theText)) (return temp1))
				(= theText (+ theText x))
				(++ temp1)
			)
		)
	)
	
	(method (at param1)
		(return (+ text (* x param1)))
	)
	
	(method (advance param1 &tmp temp0)
		(if (not (StrAt cursor 0)) (return))
		(= temp0 0)
		(while (and param1 (StrAt cursor x))
			(= temp0 1)
			(= cursor (+ cursor x))
			(if (< (+ mark 1) y)
				(++ mark)
			else
				(= lsTop (+ lsTop x))
			)
			(-- param1)
		)
		(if temp0 (self draw:))
	)
	
	(method (retreat param1 &tmp temp0)
		(= temp0 0)
		(while (and param1 (!= cursor text))
			(= temp0 1)
			(= cursor (- cursor x))
			(if mark (-- mark) else (= lsTop (- lsTop x)))
			(-- param1)
		)
		(if temp0 (self draw:))
	)
)

(class Dialog of List
	(properties
		elements 0
		size 0
		text 0
		window 0
		theItem 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		time 0
		busy 0
		caller 0
		seconds 0
		lastSeconds 0
	)
	
	(method (doit param1 &tmp temp0 newEvent temp2 temp3 temp4)
		(= temp0 0)
		(= busy 1)
		(self eachElementDo: #init)
		(if theItem (theItem select: 0))
		(= theItem
			(if (and argc param1)
				param1
			else
				(self firstTrue: #checkState 1)
			)
		)
		(if theItem (theItem select: 1))
		(if (not theItem)
			(= temp3 60)
			(= temp4 (GetTime))
		else
			(= temp3 0)
		)
		(= temp2 0)
		(while (not temp2)
			(self eachElementDo: #cycle)
			(GlobalToLocal (= newEvent (Event new:)))
			(if temp3
				(-- temp3)
				(if (== (newEvent type?) 1) (newEvent type: 0))
				(while (== temp4 (GetTime))
				)
				(= temp4 (GetTime))
			)
			(= temp2 (self handleEvent: newEvent))
			(newEvent dispose:)
			(self check:)
			(if (or (== temp2 -1) (not busy))
				(= temp2 0)
				(EditControl theItem 0)
				(break)
			)
			(Wait 1)
		)
		(= busy 0)
		(return temp2)
	)
	
	(method (dispose &tmp theCaller)
		(if (== self modelessDialog)
			(SetPort modelessPort)
			(= modelessDialog 0)
			(= modelessPort 0)
		)
		(if window (window dispose:))
		(= theItem (= window 0))
		(= theCaller caller)
		(super dispose:)
		(if theCaller (theCaller cue:))
	)
	
	(method (open param1 param2)
		(if (and (PicNotValid) cast)
			(Animate (cast elements?) 0)
		)
		(= window (window new:))
		(window
			top: nsTop
			left: nsLeft
			bottom: nsBottom
			right: nsRight
			title: text
			type: param1
			priority: param2
			open:
		)
		(= seconds time)
		(self draw:)
	)
	
	(method (draw)
		(self eachElementDo: #draw)
	)
	
	(method (cue)
		(if (not busy) (self dispose:) else (= busy 0))
	)
	
	(method (advance &tmp temp0 dialogFirst)
		(if theItem
			(theItem select: 0)
			(= dialogFirst (self contains: theItem))
			(repeat
				(if (not (= dialogFirst (self next: dialogFirst)))
					(= dialogFirst (self first:))
				)
				(= theItem (NodeValue dialogFirst))
				(if (& (theItem state?) $0001) (break))
			)
			(theItem select: 1)
		)
	)
	
	(method (retreat &tmp temp0 dialogLast)
		(if theItem
			(theItem select: 0)
			(= dialogLast (self contains: theItem))
			(repeat
				(if (not (= dialogLast (self prev: dialogLast)))
					(= dialogLast (self last:))
				)
				(= theItem (NodeValue dialogLast))
				(if (& (theItem state?) $0001) (break))
			)
			(theItem select: 1)
		)
	)
	
	(method (move param1 param2)
		(= nsRight (+ nsRight param1))
		(= nsLeft (+ nsLeft param1))
		(= nsTop (+ nsTop param2))
		(= nsBottom (+ nsBottom param2))
	)
	
	(method (moveTo param1 param2)
		(self move: (- param1 nsLeft) (- param2 nsTop))
	)
	
	(method (center)
		(self
			moveTo:
				(+
					(window brLeft?)
					(/
						(-
							(- (window brRight?) (window brLeft?))
							(- nsRight nsLeft)
						)
						2
					)
				)
				(+
					(window brTop?)
					(/
						(-
							(- (window brBottom?) (window brTop?))
							(- nsBottom nsTop)
						)
						2
					)
				)
		)
	)
	
	(method (setSize &tmp dialogFirst temp1 [theNsTop 4])
		(if text
			(TextSize @[theNsTop 0] text 0 -1)
			(= nsTop [theNsTop 0])
			(= nsLeft [theNsTop 1])
			(= nsBottom [theNsTop 2])
			(= nsRight [theNsTop 3])
		else
			(= nsRight (= nsBottom (= nsLeft (= nsTop 0))))
		)
		(= dialogFirst (self first:))
		(while dialogFirst
			(if
			(< ((= temp1 (NodeValue dialogFirst)) nsLeft?) nsLeft)
				(= nsLeft (temp1 nsLeft?))
			)
			(if (< (temp1 nsTop?) nsTop) (= nsTop (temp1 nsTop?)))
			(if (> (temp1 nsRight?) nsRight)
				(= nsRight (temp1 nsRight?))
			)
			(if (> (temp1 nsBottom?) nsBottom)
				(= nsBottom (temp1 nsBottom?))
			)
			(= dialogFirst (self next: dialogFirst))
		)
		(= nsRight (+ nsRight 4))
		(= nsBottom (+ nsBottom 4))
		(self moveTo: 0 0)
	)
	
	(method (handleEvent event &tmp theTheItem)
		(if
			(or
				(event claimed?)
				(== (event type?) nullEvt)
				(and
					(!= mouseDown (event type?))
					(!= keyDown (event type?))
					(!= evJOYSTICK (event type?))
					(!= joyDown (event type?))
				)
			)
			(EditControl theItem event)
			(return 0)
		)
		(if
		(= theTheItem (self firstTrue: #handleEvent event))
			(EditControl theItem 0)
			(if (not (theTheItem checkState: 2))
				(if theItem (theItem select: 0))
				((= theItem theTheItem) select: 1)
				(theTheItem doit:)
				(= theTheItem 0)
			)
		else
			(= theTheItem 0)
			(cond 
				(
					(and
						(or
							(== (event type?) joyDown)
							(and
								(== keyDown (event type?))
								(== ENTER (event message?))
							)
						)
						theItem
						(theItem checkState: 1)
					)
					(= theTheItem theItem)
					(EditControl theItem 0)
					(event claimed: 1)
				)
				(
					(or
						(and
							(not (self firstTrue: #checkState 1))
							(or
								(and
									(== keyDown (event type?))
									(== ENTER (event message?))
								)
								(== mouseDown (event type?))
								(== joyDown (event type?))
							)
						)
						(and
							(== keyDown (event type?))
							(== ESC (event message?))
						)
					)
					(event claimed: 1)
					(= theTheItem -1)
				)
				(
					(and
						(== keyDown (event type?))
						(== TAB (event message?))
					)
					(event claimed: 1)
					(self advance:)
				)
				(
					(and
						(== keyDown (event type?))
						(== SHIFTTAB (event message?))
					)
					(event claimed: 1)
					(self retreat:)
				)
				(else (EditControl theItem event))
			)
		)
		(return theTheItem)
	)
	
	(method (check &tmp theLastSeconds)
		(if
			(and
				seconds
				(!= lastSeconds (= theLastSeconds (GetTime 1)))
			)
			(= lastSeconds theLastSeconds)
			(if (not (-- seconds)) (self cue:))
		)
	)
)

(class Controls of List
	(properties
		elements 	0
		size 		0
	)
	
	(method (draw)
		(self eachElementDo: #setSize)
		(self eachElementDo: #draw)
	)
	
	(method (handleEvent evt &tmp cont)
		;; Find and track an active control.
		
		(if (evt claimed?) (return NULL))
		(if
			(and
				(= cont (self firstTrue: #handleEvent evt))
				(not (cont checkState: dExit))
			)
			(cont doit:)
			(= cont NULL)
		)
		(return cont)
	)
)
