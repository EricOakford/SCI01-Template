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
		sightAngle ftrDefault
		closeRangeDist ftrDefault
		longRangeDist ftrDefault
		shiftClick ftrDefault
		contClick ftrDefault
		actions ftrDefault
		control ftrDefault
		verbChecks1 ftrDefault
		verbChecks2 ftrDefault
		verbChecks3 ftrDefault
		lookStr 0
	)
	
	(procedure (localproc_0004 param1)
		(switch param1
			(verbLook
				(if lookStr
					(Print lookStr)
				else
					(Printf TEXT 0 description description)
				)
			)
			(verbOpen (Printf FEATURE 1 description))
			(verbClose (Printf FEATURE 2 description))
			(verbSmell (Printf FEATURE 3 description))
			(verbMove (Printf FEATURE 4 description))
			(verbEat (Printf FEATURE 5 description))
			(verbGet (Printf FEATURE 6 description))
			(verbClimb (Printf FEATURE 7 description))
			(verbTalk (Printf FEATURE 8 description))
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
			((event claimed?) (return 1))
			((not description) (return 0))
		)
		(switch (event type?)
			(saidEvent
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
						(event claimed: 1)
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
		(Printf FEATURE 9 description)
	)
	
	(method (notInNear)
		(Printf FEATURE 10 description)
	)
	
	(method (notFacing &tmp temp0)
		(Printf FEATURE 11 description)
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
				(return TRUE)
			else
				(self notInNear:)
				(return FALSE)
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
				(return 1)
			else
				(self notInFar:)
				(return 0)
			)
		)
	)
	
	(method (isNotHidden)
		(return 1)
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
		(if (== (param1 sightAngle?) ftrDefault)
			(param1 sightAngle: 90)
		)
		(if (== (param1 closeRangeDist?) ftrDefault)
			(param1 closeRangeDist: 50)
		)
		(if (== (param1 longRangeDist?) ftrDefault)
			(param1 longRangeDist: 100)
		)
		(if (== (param1 shiftClick?) ftrDefault)
			(param1 shiftClick: -32767)
		)
		(if (== (param1 contClick?) ftrDefault)
			(param1 contClick: 7)
		)
		(if (== (param1 actions?) ftrDefault) (param1 actions: 0))
		(if (== (param1 control?) ftrDefault) (param1 control: 0))
		(if (== (param1 verbChecks1?) ftrDefault)
			(param1 verbChecks1: -17483)
		)
		(if (== (param1 verbChecks2?) ftrDefault)
			(param1 verbChecks2: -17477)
		)
		(if (== (param1 verbChecks3?) ftrDefault)
			(param1 verbChecks3: -17477)
		)
	)
)
