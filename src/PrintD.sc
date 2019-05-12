;;; Sierra Script 1.0 - (do not remove this comment)
(script# 940)
(include sci.sh)
(use Main)
(use Intrface)

(public
	PrintD 0
)

(procedure (PrintD param1 &tmp temp0 newDialog newDText newDTextNsRight temp4 newDTextNsBottom temp6 temp7 temp8 temp9 temp10 temp11 temp12 temp13 temp14 temp15)
	(= temp11 (= temp12 -1))
	(= newDTextNsRight
		(= temp4 (= newDTextNsBottom (= temp6 0)))
	)
	(= temp7 1)
	(= temp13 0)
	(= temp15 0)
	((= newDialog (Dialog new:)) window: systemWindow)
	(= temp0 0)
	(while (< temp0 argc)
		(switch (= temp9 [param1 temp0])
			(101
				(= newDTextNsBottom (newDText nsBottom?))
				(= newDTextNsRight 0)
			)
			(67
				(= temp11 [param1 (++ temp0)])
				(= temp12 [param1 (++ temp0)])
			)
			(80
				(= temp13 [param1 (++ temp0)])
			)
			(116
				(= temp15 [param1 (++ temp0)])
			)
			(else 
				(++ temp0)
				(switch temp9
					(26
						((= newDText (DText new:)) text: [param1 temp0])
					)
					(81
						((= newDText (DButton new:))
							text: [param1 temp0]
							value: (++ temp7)
						)
					)
					(41
						((= newDText (DEdit new:))
							text: [param1 temp0]
							max: [param1 (++ temp0)]
						)
					)
					(else 
						((= newDText (DText new:)) text: [param1 (-- temp0)])
					)
				)
				(if
				(and (< (+ temp0 1) argc) (== [param1 (+ temp0 1)] 4))
					(++ temp0)
					(= newDTextNsRight
						(+ newDTextNsRight [param1 (++ temp0)])
					)
				)
				(if
				(and (< (+ temp0 1) argc) (== [param1 (+ temp0 1)] 3))
					(++ temp0)
					(= newDTextNsBottom
						(+ newDTextNsBottom [param1 (++ temp0)])
					)
				)
				(newDText
					setSize:
					moveTo: (+ newDTextNsRight 4) (+ newDTextNsBottom 4)
				)
				(newDialog add: newDText)
				(= newDTextNsRight (newDText nsRight?))
			)
		)
		(++ temp0)
	)
	(newDialog setSize: center:)
	(newDialog
		moveTo:
			(if (== -1 temp11) (newDialog nsLeft?) else temp11)
			(if (== -1 temp12) (newDialog nsTop?) else temp12)
	)
	(if temp13 (newDialog text: temp13))
	(= temp14 (newDialog at: temp15))
	(if (not (& $0001 (temp14 state?))) (= temp14 0))
	(= temp8
		(newDialog open: (if temp13 4 else 0) -1 doit: temp14)
	)
	(if (IsObject temp8)
		(if (temp8 isKindOf: DButton)
			(= temp8 (temp8 value?))
		else
			(= temp8 1)
		)
	)
	(newDialog dispose:)
	(return temp8)
)
