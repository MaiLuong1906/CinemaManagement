<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="chat-widget-container" id="chatWidgetContainer">
    <!-- Chat Toggle Button -->
    <button class="chat-toggle-btn" id="chatToggleBtn" onclick="toggleChat()">
        <i class="fa-solid fa-robot forced-fa6"></i>
        <span class="chat-badge" id="chatBadge" style="display: none;">1</span>
    </button>

    <!-- Chat Box -->
    <div class="chat-box" id="chatBox">
        <div class="chat-header">
            <div class="d-flex align-items-center gap-2">
                <div class="bot-avatar">
                    <i class="fa-solid fa-robot text-white forced-fa6"></i>
                </div>
                <div>
                    <h6 class="mb-0 fw-bold text-white">Cine AI Assistant</h6>
                    <small class="text-white-50">Online</small>
                </div>
            </div>
            <div class="d-flex gap-2">
                <button class="btn btn-sm text-white-50 hover-white" onclick="resetChat()" title="Reset session">
                    <i class="fa-solid fa-rotate-right"></i>
                </button>
                <button class="btn btn-sm text-white-50 hover-white" onclick="toggleChat()">
                    <i class="fa-solid fa-times"></i>
                </button>
            </div>
        </div>

        <div class="chat-messages" id="chatMessages">
            <div class="message bot-message">
                <div class="message-avatar">
                    <i class="fa-solid fa-robot"></i>
                </div>
                <div class="message-content">
                    Xin chào! Tôi là trợ lý AI của rạp chiếu phim. Bạn cần hỗ trợ gì hôm nay (tìm phim, lịch chiếu, đặt vé...)?
                </div>
            </div>
        </div>

        <div class="chat-input-area">
            <form id="chatForm" onsubmit="handleChatSubmit(event)">
                <div class="input-group">
                    <input type="text" id="chatInput" class="form-control" placeholder="Nhập tin nhắn..." autocomplete="off">
                    <button class="btn btn-primary" type="submit" id="sendBtn">
                        <i class="fa-solid fa-paper-plane"></i>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<style>
/* Chat Widget Styles */
.chat-widget-container {
    position: fixed;
    bottom: 30px;
    right: 30px;
    z-index: 9999;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.chat-toggle-btn {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    color: white;
    font-size: 24px;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
    cursor: pointer;
    transition: transform 0.3s ease;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
}

.chat-toggle-btn:hover {
    transform: scale(1.1);
}

.chat-badge {
    position: absolute;
    top: -5px;
    right: -5px;
    background: #ff4757;
    color: white;
    font-size: 12px;
    font-weight: bold;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 2px solid #0a0a0a;
}

.chat-box {
    position: absolute;
    bottom: 80px;
    right: 0;
    width: 350px;
    height: 500px;
    background: #1a1a2e;
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0,0,0,0.5);
    display: none;
    flex-direction: column;
    overflow: hidden;
    border: 1px solid rgba(255,255,255,0.1);
    transform-origin: bottom right;
    animation: scaleIn 0.3s ease;
}

.chat-box.active {
    display: flex;
}

@keyframes scaleIn {
    from { transform: scale(0); opacity: 0; }
    to { transform: scale(1); opacity: 1; }
}

.chat-header {
    background: linear-gradient(135deg, #16222A 0%, #3A6073 100%);
    padding: 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid rgba(255,255,255,0.1);
}

.bot-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    display: flex;
    align-items: center;
    justify-content: center;
}

.chat-messages {
    flex: 1;
    padding: 15px;
    overflow-y: auto;
    background: #0f0f1a;
    display: flex;
    flex-direction: column;
    gap: 15px;
}

/* Custom scrollbar */
.chat-messages::-webkit-scrollbar { width: 6px; }
.chat-messages::-webkit-scrollbar-track { background: transparent; }
.chat-messages::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.2); border-radius: 10px; }

.message {
    max-width: 85%;
    display: flex;
    flex-direction: column;
}

.user-message {
    align-self: flex-end;
}

.bot-message {
    align-self: flex-start;
}

.message-content {
    padding: 10px 14px;
    border-radius: 15px;
    font-size: 14px;
    line-height: 1.4;
    word-wrap: break-word;
}

.user-message .message-content {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border-bottom-right-radius: 4px;
}

.bot-message .message-content {
    background: #2a2a40;
    color: #e0e0e0;
    border-bottom-left-radius: 4px;
    border: 1px solid rgba(255,255,255,0.05);
}

/* Typing indicator */
.typing-indicator {
    display: flex;
    gap: 4px;
    padding: 14px 18px;
    background: #2a2a40;
    border-radius: 15px;
    border-bottom-left-radius: 4px;
    width: fit-content;
}

.typing-dot {
    width: 6px;
    height: 6px;
    background: #8892b0;
    border-radius: 50%;
    animation: typing 1.4s infinite ease-in-out both;
}

.typing-dot:nth-child(1) { animation-delay: -0.32s; }
.typing-dot:nth-child(2) { animation-delay: -0.16s; }

@keyframes typing {
    0%, 80%, 100% { transform: scale(0); }
    40% { transform: scale(1); }
}

/* Message Avatar */
.message-avatar {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    color: white;
    flex-shrink: 0;
    margin-bottom: 4px;
}

/* Force FA6 rendering */
.forced-fa6 {
    font-family: "Font Awesome 6 Free" !important;
    font-weight: 900 !important;
    display: inline-block !important;
    font-style: normal !important;
    font-variant: normal !important;
    text-rendering: auto !important;
    -webkit-font-smoothing: antialiased !important;
}

.bot-message {
    flex-direction: row !important;
    gap: 8px;
}

.user-message {
    align-items: flex-end;
}

/* Interactive Actions (JSON payload) */
.action-card {
    background: rgba(102, 126, 234, 0.1);
    border: 1px solid #667eea;
    border-radius: 10px;
    padding: 12px;
    margin-top: 10px;
}

.action-btn {
    width: 100%;
    margin-top: 8px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    padding: 8px;
    color: white;
    border-radius: 6px;
    font-weight: 600;
    font-size: 13px;
    transition: opacity 0.3s;
}

.action-btn:hover { opacity: 0.9; }

.chat-input-area {
    padding: 15px;
    background: #1a1a2e;
    border-top: 1px solid rgba(255,255,255,0.1);
}

.chat-input-area .form-control {
    background: rgba(255,255,255,0.05);
    border: 1px solid rgba(255,255,255,0.1);
    color: white;
}

.chat-input-area .form-control:focus {
    background: rgba(255,255,255,0.08);
    border-color: #667eea;
    box-shadow: none;
    color: white;
}

.chat-input-area .btn-primary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
}

.hover-white:hover {
    color: white !important;
}

@media (max-width: 480px) {
    .chat-box {
        width: calc(100vw - 40px);
        bottom: 90px;
        right: 20px;
        height: 60vh;
    }
}
</style>

<script>
    // Context Path from global or default logic
    const chatContextPath = '${pageContext.request.contextPath}';
    console.log("Cine AI Widget initialized with context path:", chatContextPath);

    function toggleChat() {
        const chatBox = document.getElementById('chatBox');
        chatBox.classList.toggle('active');
        if(chatBox.classList.contains('active')) {
            document.getElementById('chatBadge').style.display = 'none';
            document.getElementById('chatInput').focus();
        }
    }

    function appendMessage(sender, content, isHtml = false) {
        const chatMessages = document.getElementById('chatMessages');
        const msgDiv = document.createElement('div');
        msgDiv.className = 'message ' + (sender === 'user' ? 'user-message' : 'bot-message');
        
        if (sender === 'bot') {
            const avatarDiv = document.createElement('div');
            avatarDiv.className = 'message-avatar';
            avatarDiv.innerHTML = '<i class="fa-solid fa-robot forced-fa6"></i>';
            msgDiv.appendChild(avatarDiv);
        }

        const contentDiv = document.createElement('div');
        contentDiv.className = 'message-content';
        
        if (isHtml) {
            contentDiv.innerHTML = content;
        } else {
            contentDiv.textContent = content;
        }
        
        msgDiv.appendChild(contentDiv);
        chatMessages.appendChild(msgDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
        return contentDiv; 
    }

    function showTyping() {
        const chatMessages = document.getElementById('chatMessages');
        const typingDiv = document.createElement('div');
        typingDiv.className = 'message bot-message typing-indicator-wrapper';
        typingDiv.id = 'typingIndicator';
        typingDiv.innerHTML = `
            <div class="typing-indicator">
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
            </div>
        `;
        chatMessages.appendChild(typingDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    function removeTyping() {
        const typingItem = document.getElementById('typingIndicator');
        if (typingItem) typingItem.remove();
    }

    async function handleChatSubmit(e) {
        e.preventDefault();
        const input = document.getElementById('chatInput');
        const message = input.value.trim();
        if (!message) return;

        input.value = '';
        appendMessage('user', message);
        document.getElementById('sendBtn').disabled = true;
        
        // Start streaming logic
        showTyping();
        
        try {
            // Initiate SSE connection
            const response = await fetch(chatContextPath + '/ChatServlet/stream', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({ 'message': message })
            });

            if (!response.ok) {
                const errorText = await response.text().catch(() => "Unknown error");
                throw new Error(`Server returned status \${response.status}: \${errorText}`);
            }

            removeTyping();
            
            // Render a placeholder bot message for streaming text
            const botMessageContainer = appendMessage('bot', '');
            let accumulatedJson = ""; // For structured JSON fallback

            // Read SSE stream
            const reader = response.body.getReader();
            const decoder = new TextDecoder('utf-8');
            let done = false;
            let buffer = ""; // Buffer for partial lines

            while (!done) {
                const { value, done: readerDone } = await reader.read();
                done = readerDone;
                if (value) {
                    buffer += decoder.decode(value, { stream: !done });
                    const lines = buffer.split('\n');
                    
                    // Keep the last partial line in the buffer
                    buffer = lines.pop();

                    for (const line of lines) {
                        const trimmedLine = line.trim();
                        if (!trimmedLine || !trimmedLine.startsWith('data: ')) continue;
                        
                        const dataStr = trimmedLine.substring(6);
                        try {
                            const parsed = JSON.parse(dataStr);
                            if (parsed.status === 'complete') {
                                checkStructuredJson(accumulatedJson, botMessageContainer);
                            } else if (parsed.token) {
                                accumulatedJson += parsed.token;
                                // Fix Bug #5: Safe streaming render
                                const rawToken = parsed.token;
                                if (rawToken.includes('\n')) {
                                    // Handle newlines safely
                                    const parts = rawToken.split('\n');
                                    for (let i = 0; i < parts.length; i++) {
                                        botMessageContainer.appendChild(document.createTextNode(parts[i]));
                                        if (i < parts.length - 1) {
                                            botMessageContainer.appendChild(document.createElement('br'));
                                        }
                                    }
                                } else {
                                    botMessageContainer.appendChild(document.createTextNode(rawToken));
                                }
                                document.getElementById('chatMessages').scrollTop = document.getElementById('chatMessages').scrollHeight;
                            } else if (parsed.error) {
                                botMessageContainer.innerHTML += `<div class="alert alert-danger p-2 small mt-2">AI Error: ${parsed.error}</div>`;
                            }
                        } catch (err) {
                            console.warn("SSE JSON parse error", err, dataStr);
                        }
                    }
                }
            }
            
            // Process any remaining data in buffer
            if (buffer.trim().startsWith('data: ')) {
                 const dataStr = buffer.trim().substring(6);
                 try {
                     const parsed = JSON.parse(dataStr);
                     if (parsed.token) {
                         const rawToken = parsed.token.replace(/\\n/g, '\n');
                         if (rawToken.includes('\n')) {
                             const parts = rawToken.split('\n');
                             for (let i = 0; i < parts.length; i++) {
                                 botMessageContainer.appendChild(document.createTextNode(parts[i]));
                                 if (i < parts.length - 1) botMessageContainer.appendChild(document.createElement('br'));
                             }
                         } else {
                             botMessageContainer.appendChild(document.createTextNode(rawToken));
                         }
                     }
                 } catch(e) {}
            }
            
        } catch (error) {
            removeTyping();
            appendMessage('bot', 'Xin lỗi, đã có lỗi kết nối tới Server. Vui lòng thử lại sau.', false);
            console.error('Lỗi khi gửi chat:', error);
        } finally {
            document.getElementById('sendBtn').disabled = false;
            document.getElementById('chatInput').focus();
        }
    }

    // Function to handle interactive JSON actions
    function checkStructuredJson(fullText, containerNode) {
        fullText = fullText.trim();
        if (fullText.startsWith('{') && fullText.endsWith('}')) {
            try {
                const data = JSON.parse(fullText);
                if (data.actionType === 'BOOKING_CONFIRM') {
                    // Fix Bug #4: Sync with backend payload keys
                    const details = data.data || {}; 
                    const html = `
                        <div class="mb-2">Đây là thông tin xác nhận đặt vé của bạn:</div>
                        <div class="action-card">
                            <div class="small mb-1"><strong>Phim:</strong> \${details.movieName || 'N/A'}</div>
                            <div class="small mb-1"><strong>Ghế:</strong> \${details.seats || 'N/A'}</div>
                            <div class="small mb-1"><strong>Tổng tiền:</strong> \${Number(details.total).toLocaleString()} VNĐ</div>
                            
                            <button class="action-btn" onclick="executeSilentAction('Tôi xác nhận đặt vé phim \${details.movieName.replace(/'/g, "\\'")} ghế \${details.seats}')">
                                Xác Nhận & Thanh Toán <i class="fa-solid fa-check-circle forced-fa6 ms-1"></i>
                            </button>
                            <button class="btn btn-sm btn-outline-secondary w-100 mt-2" onclick="executeSilentAction('Hủy quá trình đặt vé')">
                                Hủy Bỏ
                            </button>
                        </div>
                    `;
                    containerNode.innerHTML = html;
                }
            } catch (e) {
                // Not valid JSON, leave as text
            }
        }
    }

    async function executeSilentAction(message) {
        const input = document.getElementById('chatInput');
        input.value = message;
        document.getElementById('chatForm').dispatchEvent(new Event('submit'));
    }

    async function resetChat() {
        try {
            const resp = await fetch(chatContextPath + '/ChatServlet/reset', { method: 'POST' });
            if (resp.ok) {
                const msgList = document.getElementById('chatMessages');
                msgList.innerHTML = `
                    <div class="message bot-message">
                        <div class="message-content">
                            Phiên hội thoại đã được làm mới. Bạn cần hỗ trợ gì tiếp theo?
                        </div>
                    </div>`;
            }
        } catch(e) {
            console.error('Reset failed', e);
        }
    }
</script>
