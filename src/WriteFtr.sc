;;; Sierra Script 1.0 - (do not remove this comment)
(script# 948)
(include sci.sh)
(use Main)
(use Intrface)
(use Feature)
(use DPath)
(use File)
(use Actor)
(use System)


(local
	[local0 100]
	[local100 100]
	[local200 100]
	[local300 100]
	[local400 190]
	local590 =  1
	local591 =  1
	local592
	local593
)
(procedure (localproc_03a6 param1 &tmp newEvent temp1 temp2 newEventY newEventX newEventY_2 newEventX_2)
	(Print 948 10)
	(while (!= ((= newEvent (Event new:)) type?) 1)
		(newEvent dispose:)
	)
	(GlobalToLocal newEvent)
	(= newEventY (newEvent y?))
	(= newEventX (newEvent x?))
	(Print 948 11)
	(while (!= ((= newEvent (Event new:)) type?) 1)
		(newEvent dispose:)
	)
	(GlobalToLocal newEvent)
	(= newEventY_2 (newEvent y?))
	(= temp1
		(+
			(/ (- (= newEventX_2 (newEvent x?)) newEventX) 2)
			newEventX
		)
	)
	(= temp2 (+ (/ (- newEventY_2 newEventY) 2) newEventY))
	(param1
		x: temp1
		y: temp2
		nsLeft: newEventX
		nsTop: newEventY
		nsBottom: newEventY_2
		nsRight: newEventX_2
	)
	(if local591
		(Graph
			grDRAW_LINE
			newEventY
			newEventX
			newEventY
			newEventX_2
			1
			15
		)
		(Graph
			grDRAW_LINE
			newEventY_2
			newEventX
			newEventY_2
			newEventX_2
			1
			15
		)
		(Graph
			grDRAW_LINE
			newEventY
			newEventX
			newEventY_2
			newEventX
			1
			15
		)
		(Graph
			grDRAW_LINE
			newEventY
			newEventX_2
			newEventY_2
			newEventX_2
			1
			15
		)
		(Graph
			grUPDATE_BOX
			newEventY
			newEventX
			newEventY_2
			newEventX_2
			1
		)
	)
	(newEvent dispose:)
)

(procedure (localproc_04ce param1 &tmp newEvent)
	(param1
		view: (GetNumber {View?} 0)
		loop: (GetNumber {Loop?} 0)
		cel: (GetNumber {Cel?} 0)
		signal: 16400
		priority: 15
		init:
	)
	(if (param1 respondsTo: #illegalBits)
		(param1 illegalBits: 0)
	)
	(while (!= ((= newEvent (Event new:)) type?) 1)
		(GlobalToLocal newEvent)
		(param1 posn: (newEvent x?) (newEvent y?))
		(Animate (cast elements?) 0)
		(newEvent dispose:)
	)
	(newEvent dispose:)
)

(procedure (localproc_0572 param1)
	(File name: @sysLogPath writeString: param1 close:)
	(DisposeScript 993)
)

(procedure (localproc_058f param1)
	(param1
		noun: (GetInput @local100 30 {/noun?})
		shiftClick: (localproc_061c @local200 20 {shftClick verb?})
		contClick: (localproc_061c @local300 20 {cntrlClick verb?})
		sightAngle: (GetNumber {sight angle?} 90)
		closeRangeDist: (GetNumber {getable dist?} 50)
		longRangeDist: (GetNumber {seeable dist?} 100)
		description: (GetInput @local400 50 {description?})
	)
)

(procedure (localproc_061c param1 param2 param3)
	(GetInput param1 param2 param3)
	(return
		(cond 
			((StrCmp param1 {verbLook}) 1)
			((StrCmp param1 {verbOpen}) 2)
			((StrCmp param1 {verbClose}) 3)
			((StrCmp param1 {verbSmell}) 4)
			((StrCmp param1 {verbMove}) 5)
			((StrCmp param1 {verbEat}) 6)
			((StrCmp param1 {verbGet}) 7)
		)
	)
)

(class WriteCode
	(properties)
	
	(method (doit param1 &tmp [temp0 400] [temp400 40])
		(if (param1 isMemberOf: Feature)
			(Format
				@temp400
				948
				0
				(param1 nsLeft?)
				(param1 nsTop?)
				(param1 nsBottom?)
				(param1 nsRight?)
			)
		else
			(Format
				@temp400
				948
				1
				(param1 view?)
				(param1 loop?)
				(param1 cel?)
			)
		)
		(Format
			@temp0
			948
			2
			@local0
			(if (== local593 2)
				(PicView name?)
			else
				((param1 superClass?) name?)
			)
			(param1 x?)
			(param1 y?)
			(param1 z?)
			(param1 heading?)
			@local100
			@temp400
			@local400
			(param1 sightAngle?)
			(param1 closeRangeDist?)
			(param1 longRangeDist?)
			@local200
			@local300
		)
		(if local590
			(Print @temp0 #font 999 #title {Feature Writer V1.0})
		)
		(localproc_0572 @temp0)
		(if (param1 isMemberOf: Feature)
			(param1 dispose:)
		else
			(param1 addToPic:)
		)
	)
	
	(method (targetObj param1)
		(param1 eachElementDo: #perform self)
		(CreateObject doit:)
	)
)

(class CreateObject
	(properties)
	
	(method (doit &tmp [temp0 15] temp15 newEvent)
		(if (not local592)
			(= temp0 0)
			(GetInput @temp0 30 {Enter path and filename})
			(Format @sysLogPath @temp0)
			(Format @local200 948 3)
			(Format @local300 948 4)
			(switch
				(Print
					948
					5
					80
					{Feature Writer V1.0}
					81
					{YES}
					1
					81
					{NO}
					2
				)
				(1 (= local591 1))
				(2 (= local591 0))
			)
			(switch
				(Print
					948
					6
					80
					{Feature Writer V1.0}
					81
					{YES}
					1
					81
					{NO}
					2
				)
				(1 (= local590 1))
				(2 (= local590 0))
			)
			(= local592 1)
		)
		(if
			(not
				(= local593
					(Print
						948
						7
						80
						{Feature Writer V1.0}
						81
						{Feature}
						1
						81
						{PicView}
						2
						81
						{View}
						3
						81
						{Prop}
						4
						81
						{Actor}
						5
					)
				)
			)
			(return)
		)
		(= temp15
			(
			(switch local593
				(1 Feature)
				(2 View)
				(3 View)
				(4 Prop)
				(5 Actor)
			)
				new:
			)
		)
		(GetInput @local0 30 {Name?})
		(StrCpy @local400 @local0)
		(localproc_058f temp15)
		(if (== local593 1)
			(localproc_03a6 temp15)
		else
			(localproc_04ce temp15)
		)
		(if
			(Print
				948
				8
				80
				{Feature Writer V1.0}
				81
				{YES}
				1
				81
				{NO}
				0
			)
			(Print 948 9)
			(while (!= ((= newEvent (Event new:)) type?) 1)
				(newEvent dispose:)
			)
			(GlobalToLocal newEvent)
			(temp15 z: (- (newEvent y?) (temp15 y?)))
			(temp15 y: (newEvent y?))
			(newEvent dispose:)
		)
		(DPath doit: temp15)
	)
)
