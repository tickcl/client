function setName()
    return "Motion Graph"
end

local motions = {
    0, 0
}

local speed = 0

function onEvent(event)
    if event:getName() == "render_event" then
        if event:is2D() then
            if auraTarget.isActiveTarget() then
                pos = project.projection(auraTarget.getPosition()[1] ,auraTarget.getPosition()[2] + 1,auraTarget.getPosition()[3])
                gl11.pushMatrix()

                gl11.position((pos[1]), (pos[2]), 0)
                gl11.rotate(utils.currentTimeMillis() / 5 % 360,0,0,1)
                gl11.position(-(pos[1]), -(pos[2]), 0)

                gl11.color(color.get(0))
                display.drawImage("https://cdn.discordapp.com/attachments/1156491586012725310/1156493213109067816/target.png", pos[1] - 50, pos[2] - 50,100, 100)
                gl11.popMatrix()
            end
        end
    end
end