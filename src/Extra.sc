;;; Sierra Script 1.0 - (do not remove this comment)
(script# 988)
(include game.sh)
(use Motion)
(use Actor)


(class Extra of Prop
	(properties
		cycleSpeed 1
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
	
	(procedure (InitialCel)
		(switch pauseCel
			(-1 (Random 0 (self lastCel:)))
			(-2 (self lastCel:))
			((== cycleType 0) pauseCel)
		)
	)
	
	
	(method (init)
		(= cel (InitialCel))
		(self changeState: 0)
		(super init:)
	)
	
	(method (doit)
		(if
			(and
				(== cycleType 1)
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
		(self setCel: (InitialCel) stopUpd:)
	)
	
	(method (startExtra)
		(self changeState: 1)
	)
	
	(method (changeState newState)
		(switch (= state newState)
			(0
				(if (<= counter 0)
					(if (!= cycleType 0)
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
					((== cycleType 0)
						(self setCycle: Forward)
						(= cycles (Random minCycles maxCycles))
					)
					((and (== cycleType 2) (== pauseCel -2)) (self setCycle: BegLoop self))
					(else (self setCycle: EndLoop self))
				)
			)
			(2
				(if (and hesitation (== cycleType 2))
					(= cycles hesitation)
				else
					(self cue:)
				)
			)
			(3
				(if (== cycleType 2)
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
				(self setCel: (InitialCel))
				(self changeState: 0)
			)
		)
	)
)
