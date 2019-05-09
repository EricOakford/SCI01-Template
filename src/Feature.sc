;;; Sierra Script 1.0 - (do not remove this comment)
(script# FEATURE)
(include game.sh)
(use Main)
(use Intrface)
(use System)


(class Feature of Object
	(properties
		x 0
		y 0
		z 0
		heading 0
		noun 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		description 0
		sightAngle 26505
		closeRangeDist 26505
		longRangeDist 26505
		shiftClick 26505
		contClick 26505
		actions 26505
		control 26505
		verbChecks1 26505
		verbChecks2 26505
		verbChecks3 26505
		lookStr 0
	)
	
	(procedure (localproc_0004 theVerb)
		(switch theVerb
			(verbLook
				(if lookStr
					(Print lookStr)
				else
					(Printf "The %s looks like any other %s." description description)
				)
			)
			(verbOpen (Printf "You cannot open the %s." description))
			(verbClose (Printf "You cannot close the %s." description))
			(verbSmell (Printf "The %s has no smell." description))
			(verbMove (Printf "You cannot move the %s." description))
			(verbEat (Printf "You wouldn't want to eat the %s." description))
			(verbGet (Printf "You cannot get the %s." description))
			(verbClimb (Printf "You can't climb the %s." description))
			(verbTalk (Printf "The %s has nothing to say." description))
		)
	)
	
	
	(method (init param1)
		(cond 
			((and argc param1) (self perform: param1))
			(ftrInitializer (self perform: ftrInitializer))
			(else (self perform: dftFtrInit))
		)
		(if (self respondsTo: #underBits)
			(cast add: self)
		else
			(features add: self)
		)
	)
	
	(method (dispose)
		(features delete: self)
		(super dispose:)
	)
	
	(method (handleEvent event &tmp temp0 temp1)
		(cond 
			((event claimed?) (return TRUE))
			((not description) (return FALSE))
		)
		(switch (event type?)
			(evSAID
				(cond 
					((not (Said noun)))
					((not (= temp1 (event message?))) (event claimed: 0))
					((self passedChecks: temp1) (self doVerb: temp1))
					((IsObject actions) (actions handleEvent: event self))
				)
			)
			(mouseDown
				(cond 
					((not (& (= temp0 (event modifiers?)) $0007)))
					((not (self onMe: event)))
					((& temp0 $0003)
						(if
							(or
								(& shiftClick $8000)
								(self passedChecks: shiftClick)
							)
							(self doVerb: (& $7fff shiftClick))
						)
						(event claimed: TRUE)
					)
					((& temp0 $0004)
						(if
						(or (& contClick $8000) (self passedChecks: contClick))
							(self doVerb: (& $7fff contClick))
						)
						(event claimed: TRUE)
					)
				)
			)
		)
		(return (event claimed?))
	)
	
	(method (setChecks param1 param2 &tmp temp0 temp1)
		(= temp0
			(<< param2 (= temp1 (* 4 (mod (- param1 1) 4))))
		)
		(cond 
			((<= param1 4)
				(= verbChecks1 (& verbChecks1 (~ (<< $000f temp1))))
				(= verbChecks1 (| verbChecks1 temp0))
			)
			((<= param1 8)
				(= verbChecks2 (& verbChecks2 (~ (<< $000f temp1))))
				(= verbChecks2 (| verbChecks2 temp0))
			)
			(else
				(= verbChecks3 (& verbChecks3 (~ (<< $000f temp1))))
				(= verbChecks3 (| verbChecks3 temp0))
			)
		)
	)
	
	(method (doVerb theVerb)
		(if doVerbCode
			(self perform: doVerbCode theVerb)
		else
			(localproc_0004 theVerb description)
		)
	)
	
	(method (notInFar)
		(Printf "You don't see the %s." description)
	)
	
	(method (notInNear)
		(Printf "You're not close enough." description)
	)
	
	(method (notFacing &tmp temp0)
		(Printf "You're not facing the %s." description)
	)
	
	(method (facingMe param1 &tmp temp0 temp1)
		(= temp0 (if argc param1 else ego))
		(if
			(>
				(= temp1
					(Abs
						(-
							(GetAngle (temp0 x?) (temp0 y?) x y)
							(temp0 heading?)
						)
					)
				)
				180
			)
			(= temp1 (- 360 temp1))
		)
		(return
			(if (<= temp1 sightAngle)
				(return TRUE)
			else
				(self notFacing:)
				(return FALSE)
			)
		)
	)
	
	(method (nearCheck param1 &tmp temp0)
		(= temp0 (if argc param1 else ego))
		(return
			(if
				(<=
					(GetDistance (temp0 x?) (temp0 y?) x y)
					closeRangeDist
				)
				(return 1)
			else
				(self notInNear:)
				(return 0)
			)
		)
	)
	
	(method (farCheck param1 &tmp temp0)
		(= temp0 (if argc param1 else ego))
		(return
			(if
				(<=
					(GetDistance (temp0 x?) (temp0 y?) x y)
					longRangeDist
				)
				(return TRUE)
			else
				(self notInFar:)
				(return FALSE)
			)
		)
	)
	
	(method (isNotHidden)
		(return TRUE)
	)
	
	(method (onMe param1 param2 &tmp temp0 temp1)
		(if (IsObject param1)
			(= temp0 (param1 x?))
			(= temp1 (param1 y?))
		else
			(= temp0 param1)
			(= temp1 param2)
		)
		(return
			(if
				(and
					(<= nsLeft temp0)
					(<= temp0 nsRight)
					(<= nsTop temp1)
					(<= temp1 nsBottom)
				)
				(if control
					(& control (OnControl 4 temp0 temp1))
				else
					1
				)
			else
				0
			)
		)
	)
	
	(method (passedChecks param1 &tmp temp0)
		(if
			(and
				(or
					(not
						(&
							(= temp0
								(&
									(>>
										(if (<= param1 4) verbChecks1 else verbChecks2)
										(* 4 (mod (- param1 1) 4))
									)
									$000f
								)
							)
							$0008
						)
					)
					(self isNotHidden:)
				)
				(or (not (& temp0 $0004)) (self farCheck:))
				(or (not (& temp0 $0002)) (self nearCheck:))
			)
			(if (not (& temp0 $0001)) else (self facingMe:))
		)
	)
)

(instance dftFtrInit of Code
	(properties)
	
	(method (doit param1)
		(if (== (param1 sightAngle?) 26505)
			(param1 sightAngle: 90)
		)
		(if (== (param1 closeRangeDist?) 26505)
			(param1 closeRangeDist: 50)
		)
		(if (== (param1 longRangeDist?) 26505)
			(param1 longRangeDist: 100)
		)
		(if (== (param1 shiftClick?) 26505)
			(param1 shiftClick: -32767)
		)
		(if (== (param1 contClick?) 26505)
			(param1 contClick: 7)
		)
		(if (== (param1 actions?) 26505) (param1 actions: 0))
		(if (== (param1 control?) 26505) (param1 control: 0))
		(if (== (param1 verbChecks1?) 26505)
			(param1 verbChecks1: -17483)
		)
		(if (== (param1 verbChecks2?) 26505)
			(param1 verbChecks2: -17477)
		)
		(if (== (param1 verbChecks3?) 26505)
			(param1 verbChecks3: -17477)
		)
	)
)
