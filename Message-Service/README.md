1. Search account by email 
    
    -> tìm ra được account đó (user_id)
2. Gọi API create room_chat -> params(user_id, friend_id)

   (Có rồi thì không cần tạo, trả về room_chat_id)

    -> create được room_chat (room_chat_id)
   
    -> create được 2 record room_chat_user (type, user_id, room_chat_id), (type, friend_id, room_chat_id)
   
    ---> Tạo được relation giữa 2 account
3. Gửi message params(user_id, room_chat_id, message_content)
   
   -> create được message