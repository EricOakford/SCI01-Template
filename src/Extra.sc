;;; Sierra Script 1.0 - (do not remove this comment)
(script# EXTRA)
(include game.sh)
(use Motion)
(use Actor)


(class Extra of Prop
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
		yStep 2
		view 0
		loop 0
		cel 0
		priority 0
		underBits 0
		signal $0000
		lsTop 0
		lsLeft 0
		lsBottom 0
		lsRight 0
		brTop 0
		brLeft 0
		brBottom 0
		brRight 0
		palette 0
		cycleSpeed 1
		script 0
		cycler 0
		timer 0
		cycleType 0
		hesitation 0
		pauseCel 0
		minPause 10
		maxPause 30
		minCycles 8
		maxCycles 20
		counter 0
		state $ffff
		cycles 0
	)
	
	(procedure (GetPausedCel)
		(switch pauseCel
			(ExtraRandomCel (Random 0 (self lastCel:)))
			(ExtraLastCel (self lastCel:))
			((== cycleType 0) pauseCel)
		)
	)
	
	
	(method (init)
		(= cel (GetPausedCel))
		(self changeState: 0)
		(super init:)
	)
	
	(method (doit)
		(if
			(and
				(== cycleType ExtraEndLoop)
				(== cel pauseCel)
				(!= pauseCel 0)
			)
			(= cycles (+ hesitation 1))
		)
		(if (and cycles (not (-- cycles))) (self cue:))
		(super doit:)
	)
	
	(method (cue)
		(if (& signal $0005)
		else
			(self changeState: (+ state 1))
		)
	)
	
	(method (stopExtra)
		(self setCel: (GetPausedCel) stopUpd:)
	)
	
	(method (startExtra)
		(self changeState: 1)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (<= counter 0)
					(if (!= cycleType ExtraForward)
						(= counter (- (Random minCycles maxCycles) 1))
					)
					(if (not (= cycles (Random minPause maxPause)))
						(self cue:)
					)
				else
					(-- counter)
					(self cue:)
				)
			)
			(1
				(cond 
					((== cycleType ExtraForward)
						(self setCycle: Forward)
						(= cycles (Random minCycles maxCycles))
					)
					((and (== cycleType 2) (== pauseCel -2)) (self setCycle: BegLoop self))
					(else (self setCycle: EndLoop self))
				)
			)
			(2
				(if (and hesitation (== cycleType ExtraEndAndBeginLoop))
					(= cycles hesitation)
				else
					(self cue:)
				)
			)
			(3
				(if (== cycleType ExtraEndAndBeginLoop)
					(if (!= pauseCel -2)
						(self setCycle: BegLoop self)
					else
						(self setCycle: EndLoop self)
					)
				else
					(self cue:)
				)
			)
			(4
				(self setCel: (GetPausedCel))
				(self changeState: 0)
			)
		)
	)
)
