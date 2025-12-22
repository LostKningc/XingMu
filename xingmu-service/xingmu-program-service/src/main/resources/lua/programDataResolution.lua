local ticket_category_list = cjson.decode(ARGV[1])
local del_seat_list = cjson.decode(ARGV[2])
local add_seat_data_list = cjson.decode(ARGV[3])

for index,increase_data in ipairs(ticket_category_list) do
    local key = KEYS[increase_data.programTicketRemainNumberHashKeyIndex]
    local ticket_category_id = increase_data.ticketCategoryId
    local increase_count = increase_data.count
    redis.call('HINCRBY',key,ticket_category_id,increase_count)
end
for index, seat in pairs(del_seat_list) do
    local key = KEYS[seat.seatHashKeyDelIndex]
    local seat_id_list = seat.seatIdList
    redis.call('HDEL',key,unpack(seat_id_list))
end
for index, seat in pairs(add_seat_data_list) do
    local key = KEYS[seat.seatHashKeyAddIndex]
    local seat_data_list = seat.seatDataList
    redis.call('HMSET',key,unpack(seat_data_list))
end