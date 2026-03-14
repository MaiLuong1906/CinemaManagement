/* =========================================================
   Chat Persistence Schema for AI Agents
   Adds long-term memory capabilities to CineGuide and CineAnalyst
   ========================================================= */

USE CinemaManagement;
GO

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='chat_messages' AND xtype='U')
BEGIN
    CREATE TABLE chat_messages (
        id INT IDENTITY(1,1) PRIMARY KEY,
        session_id VARCHAR(100) NOT NULL,
        user_id INT NULL, -- NULL if guest
        role VARCHAR(20) NOT NULL, -- 'user', 'assistant', 'system'
        content NVARCHAR(MAX) NOT NULL,
        created_at DATETIME DEFAULT GETDATE(),
        
        -- Optional foreign key if user logs out, we still keep history? 
        -- Yes, standard practice. But since user_id is nullable, we shouldn't enforce strict CASCADE unless needed.
        CONSTRAINT FK_Chat_User FOREIGN KEY (user_id) REFERENCES user_profiles(user_id) ON DELETE SET NULL
    );

    -- Indexes for fast retrieval
    CREATE INDEX idx_chat_session ON chat_messages(session_id, created_at);
    CREATE INDEX idx_chat_user ON chat_messages(user_id, created_at);

    PRINT 'Table chat_messages created successfully.';
END
ELSE
BEGIN
    PRINT 'Table chat_messages already exists.';
END
GO
