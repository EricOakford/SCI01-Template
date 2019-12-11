;;; Sierra Script 1.0 - (do not remove this comment)
(script# 954)
(include game.sh)
(use Main)
(use Intrface)
(use Approach)
(use Grooper)
(use Sight)
(use Avoider)
(use Motion)
(use User)
(use System)

(public
	GoToIfSaid 0
	TurnIfSaid 1
)

(local
	turnToWhom
	goToWhom
	oldLooper
	oldAvoider
)
(procedure (GoToIfSaid param1 param2 param3 param4 param5 &tmp temp0 temp1)
	(switch
		(= temp1
			(ISSc
				init: param1 (if (and (>= argc 5) param5) param5 else '*/*') 1 0
			)
		)
		(1
			(ego
				setMotion:
					(if (IsObject param3) Approach else MoveTo)
					param3
					param4
					ISSc
				setAvoider: Avoider
			)
		)
		(2
			(if (>= argc 6) (Print &rest))
		)
	)
	(return temp1)
)

(procedure (TurnIfSaid param1 param2 param3 &tmp temp0)
	(= temp0
		(GetAngle (ego x?) (ego y?) (param1 x?) (param1 y?))
	)
	(return
		(if
			(==
				1
				(ISSc
					init:
						param1
						(if (>= argc 3) param3 else '*/*')
						(CantBeSeen
							param1
							ego
							(/ 360 (Max 4 (* (/ (NumLoops ego) 4) 4)))
						)
						1
				)
			)
			(if (IsObject oldLooper) (oldLooper dispose:))
			(= oldLooper (ego looper?))
			(ego
				looper: 0
				heading: temp0
				setMotion: 0
				setLoop: GradualLooper
			)
			((ego looper?) doit: ego temp0 ISSc)
			1
		else
			0
		)
	)
)

(instance ISSc of Script
	(properties)
	
	(method (init theTurnToWhom param2 param3 param4)
		(return
			(if param3
				(if
					(and
						(not (if param4 turnToWhom else goToWhom))
						(Said param2)
					)
					(if (User canControl:)
						(if (IsObject oldAvoider) (oldAvoider dispose:))
						(= oldAvoider (ego avoider?))
						(ego avoider: 0)
						(if param4
							(= turnToWhom theTurnToWhom)
						else
							(= goToWhom theTurnToWhom)
						)
						(User canControl: 0 canInput: 0)
						1
					else
						((User curEvent?) claimed: 0)
						2
					)
				)
			else
				0
			)
		)
	)
	
	(method (cue &tmp newEvent)
		(User canControl: TRUE canInput: TRUE)
		((= newEvent (Event new:)) type: saidEvent)
		(Parse (User inputLineAddr?) newEvent)
		(ego setAvoider: oldAvoider)
		(= oldAvoider 0)
		(if turnToWhom
			((ego looper?) dispose:)
			(ego looper: oldLooper)
			(= oldLooper 0)
			(turnToWhom handleEvent: newEvent)
			(= turnToWhom 0)
		else
			(goToWhom handleEvent: newEvent)
			(= goToWhom 0)
		)
		(if (not (newEvent claimed?))
			(regions eachElementDo: #handleEvent newEvent 1)
			(theGame handleEvent: newEvent 1)
		)
		(newEvent dispose:)
	)
)
