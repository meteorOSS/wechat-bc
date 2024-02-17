--
-- 原来CObjectBaseM:command相关逻辑
-- 战斗中角色移动命令
--

--
-- MoveByDis
--
-- moveByDis = 1, --移动多少距离
--

local MoveByDis = class('MoveByDis', battleEffect.EventEffect)
battleEffect.MoveByDis = MoveByDis

function MoveByDis:onPlay()
	local args = self.args
	local speed = args.speed
	speed = math.max(speed, 1)
	local a = args.a
	local dis = cc.pGetLength(cc.p(args.x, args.y))
	dis = math.max(dis, 1)
	local time
	if a == 0 then
		time = dis / speed
	else
		local det = speed*speed + 2*a*dis
		det = math.max(det, 0)
		time = (math.sqrt(det) - speed) / a
	end

	local move = cc.MoveBy:create(time, cc.p(args.x, args.y))
	local action
	action = transition.speed(self.target, {speed=1, action=transition.spawnEx()
		:sequenceBegin()
			:action(move)
			:func(function ()
				self.target:runAction(move:reverse())
			end)
		:sequenceEnd()
		:func(function ()
			action:setSpeed(1 + a * move:getDuration())
		end)
		:done()
	})

	self.target:runAction(action)
	self.view:setActionState(battle.SpriteActionTable.run)
end

function MoveByDis:onUpdate(delta)
end

function MoveByDis:onStop(delta)
	self.view:setActionState(battle.SpriteActionTable.standby)
end

--
-- MoveByTime
--
-- moveByTime = 2, --移动多少时间
--

local MoveByTime = class('MoveByTime', battleEffect.EventEffect)
battleEffect.MoveByTime = MoveByTime

function MoveByTime:onPlay()
	local args = self.args
	local speed = args.speed
	local a = args.a
	local time = args.t
	local angle = args.angle
	local dis = speed*time + a*time*time/2
	local x = math.cos(math.rad(angle)) * dis
	local y = math.sin(math.rad(angle)) * dis

	local action = cc.Speed:create(cc.MoveBy:create(time, cc.p(x, y)), 2)
	self.target:runAction(action)
	self.view:setActionState(battle.SpriteActionTable.run)
end

function MoveByTime:onUpdate(delta)
end

function MoveByTime:onStop(delta)
	self.view:setActionState(battle.SpriteActionTable.standby)
end

--
-- MoveTo
--
-- moveTo = 3, --移动到
--

local MoveTo = class('MoveTo', battleEffect.EventEffect)
battleEffect.MoveTo = MoveTo

function MoveTo:onPlay()
	local args = self.args
	local speed = args.speed
	local a = args.a
	local x, y = args.x, args.y
	local x2, y2 = self.target:getCurPos()
	local dis = cc.pGetLength(cc.p(x - x2, y - y2))
	local time
	local delay = 0
	local turnBack = args.turnBack 		-- 移动时是否需要转身
	local knockUp = args.knockUp        -- 击飞效果
	local knockUpBack = args.knockUpBack -- 击飞返回
	if a == 0 then
		time = dis / speed
	else
		local det = speed*speed + 2*a*dis
		det = math.max(det, 0)
		time = (math.sqrt(det) - speed) / a
	end

	if args.costTime and args.costTime >= 0 then
		time = args.costTime / 1000 -- time参数 毫秒
	end

	if args.delayMove then
		delay = args.delayMove / 1000
	end
	-- 缩放比例
	if args.timeScale then
		time = time * args.timeScale
	end

	local move
	if knockUp then
		local faceToknockUp
		if self.target.force == 1 then
			faceToknockUp = 1
		elseif self.target.force == 2 then
			faceToknockUp = -1
		end
		local function knockUpTurn()
			faceToknockUp = faceToknockUp * -1
			self.target:setShowFaceTo(faceToknockUp)
		end
		move = cc.Spawn:create(cc.EaseIn:create(cc.MoveTo:create(time, cc.p(x, y)), 2),cc.Repeat:create(cc.Sequence:create(cc.DelayTime:create(0.25),cc.CallFunc:create(knockUpTurn)),4))
	else
		move = cc.EaseIn:create(cc.MoveTo:create(time, cc.p(x, y)), 2)
		if time == 0 then
			move = cc.CallFunc:create(function()
				self.target:setPosition(cc.p(x,y))
			end)
		end
	end
	--这里需要加一个判断obj移动到的位置在初始位置的前后来判断是否转身 在初始位置前方的需要转身
	local isTurnBack = false
	if turnBack then
		if self.target.force == 1 and x2 > x  then --force == 1 且 移动到的目标x坐标大于初始x坐标 需要转身
			isTurnBack = true
		elseif self.target.force == 2 and x2 < x then --force == 2 且 移动到的目标x坐标小于初始x坐标 需要转身
			isTurnBack = true
		end
  	end
	-- 攻击完成转身回去
	local faceTo = isTurnBack and -1 or 1
	local function changeFaceTo()
		if args.changeFaceTo then
			-- self.target:setScaleX(faceTo*args.changeFaceTo)
			self.target:setShowFaceTo(faceTo*args.changeFaceTo)
			faceTo = 1
		end
	end

	local action = cc.Sequence:create(
		cc.CallFunc:create(changeFaceTo),
		cc.DelayTime:create(delay),
		move,
		cc.CallFunc:create(changeFaceTo),
		cc.CallFunc:create(handler(self, self.stop))
	)
	self.target:runAction(action)
	if not knockUp and not knockUpBack then
		self.view:setActionState(battle.SpriteActionTable.run)
	end
	if knockUp then
		self.target:isComeBacking(true)
	elseif knockUpBack then
		self.target:isComeBacking(false)
	end


end

function MoveTo:onUpdate(delta)
end

function MoveTo:onStop(delta)
	self.view:setCurPos(cc.p(self.args.x, self.args.y))
	self.view:setActionState(battle.SpriteActionTable.standby)
end

--
-- ComeBack
--
local ComeBack = class('ComeBack', battleEffect.MoveTo)
battleEffect.ComeBack = ComeBack
function ComeBack:onPlay()
	local x, y = self.target:getSelfPos()
	self.args = {
		speed=1500,
		a=2000,
		costTime = self.args.costTime,
		delayMove = self.args.delayMove,
		x=x, y=y,
		changeFaceTo = self.target.forceFaceTo,
		turnBack = true
	}
	battleEffect.MoveTo.onPlay(self)
	self.target:isComeBacking(true)
end

function ComeBack:onStop(delta)
	battleEffect.MoveTo.onStop(self, delta)
	self.target:resetPos()
	self.target:isComeBacking(false)
end

function ComeBack:debugString()
	return string.format("ComeBack: %s", toDebugString(self.target))
end

--
-- Callback
--
-- callback = 4, --回调
--

local Callback = class("Callback", battleEffect.OnceEventEffect)
battleEffect.Callback = Callback

function Callback:onPlay()
	-- func will be contain upvalues
	-- self.args.func(self.view, self.target, self.args.args)
	-- no use view\target\args any more

	self.args.func()

	-- print("================ traceback ================")
	-- print(self.traceback)
	-- print("================ funcinfo ================")
	-- local tb = debug.getinfo(self.args.func, "nSl")
	-- print_r(tb)
end

--
-- OnceEffect
--
-- OnceEffect, -- 一次性特效
-- 显示上使用
--

local OnceEffect = class("OnceEffect", battleEffect.EventEffect)
battleEffect.OnceEffect = OnceEffect

function OnceEffect:onPlay()
	local cfg = self.args
	self.view:onViewProxyCall('onBuffPlayOnceEffect', cfg.tostrModel,
		cfg.resPath, cfg.aniName, cfg.pos, cfg.offsetPos, cfg.assignLayer, cfg.wait)
end

--
-- Wait
--
-- wait = 4, -- 等待, 外部手动关闭
-- 显示上使用，现在只有boss_info和ArenaGate:playStartAni用到
--

local Wait = class("Wait", battleEffect.EventEffect)
battleEffect.Wait = Wait

function Wait:onUpdate(delta)
end

--
-- Jump
--
-- 大招跳过
--

local Jump = class("Jump",battleEffect.OnceEventEffect)
battleEffect.Jump = Jump

function Jump:onPlay()
	if self.args.jumpFlag and self.view.skillJumpSwitchOnce then
		--print(" Jump:onPlay()",self.view.model.id)
		local battleView = gRootViewProxy:raw()
		battleView:closeEffectEventEnable()
		self.view:onCleanEffectCache()
	end
end

--
-- Follow
--
-- 让target跟随骨骼
-- scene=true, 让镜头跟随骨骼
--


local BattleViewTarget = {}
BattleViewTarget.__index = BattleViewTarget
BattleViewTarget.__targes = {
	"stageLayer",
	"gameLayer",
	-- "effectLayer",
	"effectLayerNum",
}
BattleViewTarget.__fmap = {
	getPosition = 'setPosition',
	getRotation = 'setRotation',
	getScaleX = 'setScaleX',
	getScaleY = 'setScaleY',
}

function BattleViewTarget.new()
	local ret = setmetatable({}, BattleViewTarget)
	ret:ctor()
	return ret
end

function BattleViewTarget:ctor()
	self.view = gRootViewProxy:raw()
	self.scaleX = 1
	self.scaleY = 1
	self.backups = {}
end

function BattleViewTarget:runAction(action)
	return self.view:runAction(action)
end

function BattleViewTarget:stopAction(action)
	return self.view:stopAction(action)
end

function BattleViewTarget:revert()
	for fname, t in pairs(self.backups) do
		for name, v in pairs(t) do
			local node = self.view[name]
			local f = node[self.__fmap[fname]]
			if type(v) == "table" then
				f(node, unpack(v))
			else
				f(node, v)
			end
		end
	end
	self.backups = {}
end

function BattleViewTarget:_backup(fname, nret)
	if self.backups[fname] then return end
	nret = nret or 1
	local t = {}
	for _, name in ipairs(self.__targes) do
		local node = self.view[name]
		if node then
			local f = node[fname]
			if f then
				if nret == 1 then
					t[name] = f(node)
				else
					t[name] = {f(node)}
				end
			end
		end
	end
	self.backups[fname] = t
end

function BattleViewTarget:_do(fname, ...)
	for _, name in ipairs(self.__targes) do
		local node = self.view[name]
		if node then
			local f = node[fname]
			if f then
				f(node, ...)
			end
		end
	end
end

function BattleViewTarget:setPosition(x, y)
	self:_backup('getPosition', 2)
	-- 屏幕中心 与 骨骼点 对齐
	local newX = -(x - display.cx)
	local newY = -(y - display.cy)
	return self:_do('setPosition', newX * self.scaleX, newY * self.scaleY)
end

function BattleViewTarget:setRotation(rotation)
	self:_backup('getRotation')
	return self:_do('setRotation', rotation)
end

function BattleViewTarget:setScaleX(scaleX)
	self:_backup('getScaleX')
	self.scaleX = 1.0/scaleX
	return self:_do('setScaleX', self.scaleX)
end

function BattleViewTarget:setScaleY(scaleY)
	self:_backup('getScaleY')
	self.scaleY = 1.0/scaleY
	return self:_do('setScaleY', self.scaleY)
end


local Follow = class("Follow", battleEffect.EventEffect)
battleEffect.Follow = Follow

function Follow:onPlay()
	local faceTo = self.args.faceTo
	if self.args.follow.scene then
		faceTo = 1
	end
	local bones = self.args.follow.bones
	if not self.args.processArgs then
        return
    end
	local targets = self.args.processArgs.viewTargets
	local curIndex = self.args.index
	local rnd = math.random(1, table.length(bones))
	-- 多targets均匀分配bones
	if table.length(targets) > 1 and table.length(bones) > 1 then
		local idx = itertools.first(targets, function(obj) return obj.id == self.target.id end)
		if idx then
			rnd = ((idx-1) % table.length(bones)) + 1
		end
	end

	-- print('!!!! follow', dumps(bones), rnd, bones[rnd])
	-- print(self.target, self.target.id, self.args.follow.scene)

	self.boneName = bones[rnd]
	self.boneSprite = self.args.fromSprite

	if self.boneSprite == nil then return end

	-- display.director:getScheduler():setTimeScale(0.5)
	-- self.boneSprite:debugParents()

	if self.args.follow.scene then
		self.target = BattleViewTarget.new()
		self.view = self.target
	else
		self.oldX, self.oldY = self.target:getPosition()
		self.oldRotation = self.target:getRotation()
		self.oldScaleX = self.target:getScaleX()
		self.oldScaleY = self.target:getScaleY()
		-- local shearX = self.view:getShearX()
		-- local shearY = self.view:getShearY()
	end

	local boneName = self.boneName
	local sprite = self.boneSprite.sprite
	self.boneSprite:addActionCompleteListener(function(ani, count)
		-- print('!!!! complete', ani, count)
		self:stop()
	end)
	if self.args.follow.scene and curIndex > 1 then return end -- 对于场景的follow只执行一次
	-- local draw1 = cc.DrawNode:create(4)
	-- draw1:drawDot(cc.p(0, 0), 5, cc.c4f(1, 1, 0, 1))
	-- draw1:drawLine(cc.p(0, 0), cc.p(20, 0), cc.c4f(1, 1, 0, 1))
	-- draw1:addTo(self.boneSprite, 999)

	-- local center = cc.DrawNode:create()
	-- center:drawRect(cc.p(-10, -10), cc.p(10, 10), cc.c4f(1, 0, 0, 1))
	-- center:addTo(gRootViewProxy:raw(), 99):move(display.center)

	local function update()
		local posx, posy = self.boneSprite:getPosition()
		local sx, sy = sprite:getScaleX(), sprite:getScaleY()
		local bxy = sprite:getBonePosition(boneName)
		local rotation = sprite:getBoneRotation(boneName)
		local scaleX = sprite:getBoneScaleX(boneName)
		local scaleY = sprite:getBoneScaleY(boneName)

		-- draw1:move(bxy.x * sx, bxy.y * sy)
		-- print('!! follow', bxy.x*sx, bxy.y*sy, posx, posy, scaleX, scaleY)

		bxy.x = bxy.x * sx + posx
		bxy.y = bxy.y * sy + posy

		self.target:setRotation(rotation)
		self.target:setScaleX(scaleX*faceTo, true)
		self.target:setScaleY(scaleY, true)
		self.target:setPosition(bxy.x, bxy.y)
	end

	self.action = cc.RepeatForever:create(cc.Sequence:create(
		cc.CallFunc:create(update)
	))
	self.target:runAction(self.action)
end

function Follow:onUpdate(delta)
end

function Follow:onStop()
	self.target:stopAction(self.action)
	local notBack = self.args.follow.notback
	if self.args.follow.scene then
		self.target:revert()
	elseif not notBack then
		self.target:setPosition(self.oldX, self.oldY)
		self.target:setRotation(self.oldRotation)
		self.target:setScaleX(self.oldScaleX)
		self.target:setScaleY(self.oldScaleY)
	else
		self.target:isComeBacking(true)
	end
end

--
-- Control
--
-- 控制target Sprite上的控件状态 例:血条
--
--

local Control = class("Control", battleEffect.OnceEventEffect)
battleEffect.Control = Control

function Control:onPlay()
	local battleView = gRootViewProxy:raw()
	local lifeBar = self.args.lifeBar
	if not battleView then
		errorInWindows("Control:onPlay battleView is nil")
		return
	end
	local objs = battleView:onViewProxyCall('getSceneObjs')
	for _, objSpr in maptools.order_pairs(objs) do
		if objSpr and objSpr.model and not objSpr.model:isRealDeath() then
			if lifeBar then
				objSpr.lifebar:setVisible(lifeBar.show or false)
				objSpr:onAttacting(false)
			end
		end
	end
end