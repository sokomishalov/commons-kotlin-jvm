box.cfg {}

box.once("schema", function()
    box.schema.space.create('cache')
    box.space.cache:format({
        { name = 'key', type = 'string' },
        { name = 'value', type = 'string' }
    })
    box.space.cache:create_index('primary', {
        type = 'hash',
        parts = { 'key' }
    })
end)