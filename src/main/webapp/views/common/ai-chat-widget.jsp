<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="chat-widget-container" id="chatWidgetContainer">
    <!-- Chat Toggle Button (Redesigned as Input Bar) -->
    <div class="chat-toggle-bar" id="chatToggleBtn" onclick="toggleChat()">
        <div class="chat-input-placeholder">Ask a question...</div>
        <div class="chat-action-icons">
            <span class="chat-shortcut">Ctrl+I</span>
            <div class="chat-send-icon">
                <i class="fa-solid fa-arrow-up forced-fa6"></i>
            </div>
        </div>
        <span class="chat-badge" id="chatBadge" style="display: none;">1</span>
    </div>

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
    position: fixed !important;
    bottom: 25px !important;
    left: 50% !important;
    transform: translateX(-50%) !important;
    z-index: 2147483647 !important;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif !important;
    display: flex !important;
    flex-direction: column !important;
    align-items: center !important;
    visibility: visible !important;
    opacity: 1 !important;
    width: 90% !important;
    max-width: 600px !important;
    height: auto !important;
    pointer-events: none !important;
}

.chat-toggle-bar {
    pointer-events: auto !important;
    width: 100%;
    height: 52px;
    background: #0f0f1a;
    border: 1px solid rgba(255,255,255,0.1);
    border-radius: 12px;
    padding: 0 12px 0 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    cursor: text;
    box-shadow: 0 8px 32px rgba(0,0,0,0.4);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.chat-toggle-bar:hover {
    border-color: rgba(255,255,255,0.2);
    transform: translateY(-2px);
    box-shadow: 0 12px 40px rgba(0,0,0,0.5);
}

.chat-input-placeholder {
    color: rgba(255,255,255,0.5);
    font-size: 15px;
    letter-spacing: 0.3px;
}

.chat-action-icons {
    display: flex;
    align-items: center;
    gap: 12px;
}

.chat-shortcut {
    color: rgba(255,255,255,0.3);
    font-size: 12px;
    font-weight: 500;
}

.chat-send-icon {
    width: 32px;
    height: 32px;
    background: #5d3fd3; /* Purple color from screenshot */
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 14px;
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
    pointer-events: auto !important;
    position: absolute;
    bottom: 65px;
    left: 50%;
    transform: translateX(-50%);
    width: 100%;
    max-width: 500px;
    height: 600px;
    background: #1a1a2e;
    border-radius: 16px;
    box-shadow: 0 20px 60px rgba(0,0,0,0.6);
    display: none;
    flex-direction: column;
    overflow: hidden;
    border: 1px solid rgba(255,255,255,0.1);
    transform-origin: bottom center;
    animation: scaleIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.chat-box.active {
    display: flex !important;
    opacity: 1 !important;
    transform: translateX(-50%) scale(1) !important;
}

@keyframes scaleIn {
    from { transform: translateX(-50%) scale(0.8); opacity: 0; }
    to { transform: translateX(-50%) scale(1); opacity: 1; }
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

/* --- PAYMENT CARD STYLES --- */
.payment-card {
    border: 1px solid #28a745 !important;
    background: #1e2a1e !important;
}
.payment-header {
    background: #28a745;
    color: white;
    padding: 8px 12px;
    font-size: 13px;
    font-weight: bold;
}
.payment-body {
    padding: 12px;
}
.action-card {
    background: #2a2a40;
    border-radius: 12px;
    overflow: hidden;
    margin-top: 10px;
    border: 1px solid rgba(255,255,255,0.1);
}
.action-btn {
    background: #5d3fd3;
    color: white;
    border: none;
    padding: 8px 16px;
    border-radius: 8px;
    width: 100%;
    font-weight: bold;
    margin-top: 10px;
    transition: all 0.2s;
}
.action-btn:hover { background: #4b30b5; transform: translateY(-2px); }

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
    font-family: inherit !important;
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

/* UI Widget Styles */
.widget-login {
    background: #2a2a40;
    padding: 15px;
    border-radius: 10px;
    margin-top: 10px;
    border: 1px solid rgba(255,255,255,0.05);
}
.widget-login input {
    margin-bottom: 10px;
    background: rgba(0,0,0,0.2) !important;
    border: 1px solid rgba(255,255,255,0.1) !important;
    color: white !important;
}
.widget-login input::placeholder { color: #888; }
.widget-seat-map {
    background: #1a1a2e;
    padding: 10px;
    border-radius: 10px;
    margin-top: 10px;
    border: 1px solid #667eea;
}
.screen-curve {
    height: 15px;
    border-top: 2px solid rgba(255,255,255,0.2);
    border-radius: 50% / 100% 100% 0 0;
    margin: 10px auto 20px;
    text-align: center;
    color: #888;
    font-size: 10px;
}
.seat-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 5px;
    justify-content: center;
    margin-bottom: 15px;
    max-height: 200px;
    overflow-y: auto;
}
.seat-icon {
    width: 25px;
    height: 25px;
    background: #444;
    border-radius: 5px;
    font-size: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    user-select: none;
    transition: all 0.2s;
}
.seat-icon.booked { background: #ff4757; cursor: not-allowed; }
.seat-icon.selected { background: #2ed573; color: #111; font-weight: bold; }
.seat-icon.available:hover { background: #667eea; }
.widget-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-top: 1px solid rgba(255,255,255,0.1);
    padding-top: 10px;
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
    let isHistoryLoaded = false;

    console.log("Cine AI Widget initialized with context path:", chatContextPath);

    function toggleChat() {
        console.log("toggleChat() called");
        const chatBox = document.getElementById('chatBox');
        if (!chatBox) {
            console.error("Chat box element not found!");
            return;
        }
        
        chatBox.classList.toggle('active');
        const isActive = chatBox.classList.contains('active');
        console.log("Chat box active status:", isActive);
        
        if(isActive) {
            const badge = document.getElementById('chatBadge');
            if (badge) badge.style.display = 'none';
            
            const input = document.getElementById('chatInput');
            if (input) input.focus();
            
            // Tự động tải lại lịch sử khi mở chat lần đầu
            if (!isHistoryLoaded) {
                console.log("Triggering history load...");
                loadChatHistory();
            }
        }
    }

    async function loadChatHistory() {
        try {
            const response = await fetch(chatContextPath + '/ChatServlet/history');
            const data = await response.json();
            
            if (data.success && data.history && data.history.length > 0) {
                const chatMessages = document.getElementById('chatMessages');
                // Giữ lại tin nhắn chào mừng mặc định nếu muốn, hoặc xóa hết
                chatMessages.innerHTML = ''; 
                
                data.history.forEach(msg => {
                    // ChatMessageDAO sends 'text' property, but appendMessage expects content.
                    // Fallback to msg.content just in case.
                    appendMessage(msg.role === 'assistant' ? 'bot' : 'user', msg.text || msg.content || '');
                });
                
                isHistoryLoaded = true;
                console.log("Chat history restored:", data.history.length, "messages");
            }
        } catch (error) {
            console.error("Failed to load chat history:", error);
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
        
        showTyping();
        
        try {
            const response = await fetch(chatContextPath + '/ChatServlet/stream', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({ 'message': message })
            });

            if (!response.ok) {
                const errorText = await response.text().catch(() => "Unknown error");
                throw new Error(`Server returned status ${response.status}: ${errorText}`);
            }

            removeTyping();
            
            const botMessageContainer = appendMessage('bot', '');
            let accumulatedJson = "";

            const reader = response.body.getReader();
            const decoder = new TextDecoder('utf-8');
            let done = false;
            let buffer = "";

            while (!done) {
                const { value, done: readerDone } = await reader.read();
                done = readerDone;
                if (value) {
                    buffer += decoder.decode(value, { stream: !done });
                    const lines = buffer.split('\n');
                    buffer = lines.pop();

                    for (const line of lines) {
                        const trimmedLine = line.trim();
                        if (!trimmedLine || !trimmedLine.startsWith('data: ')) continue;
                        
                        const dataStr = trimmedLine.substring(6);
                        try {
                            const parsed = JSON.parse(dataStr);
                            
                            // Check for Side-Channel Widget payload directly in stream
                            if (parsed.widget) {
                                renderWidget(parsed.widget, botMessageContainer);
                            }
                            
                            if (parsed.status === 'complete') {
                                checkStructuredJson(accumulatedJson, botMessageContainer);
                            } else if (parsed.token) {
                                accumulatedJson += parsed.token;
                                const rawToken = parsed.token;
                                if (rawToken.includes('\n')) {
                                    const parts = rawToken.split('\n');
                                    for (let i = 0; i < parts.length; i++) {
                                        let node = document.createTextNode(parts[i]);
                                        botMessageContainer.appendChild(node);
                                        if (i < parts.length - 1) {
                                            botMessageContainer.appendChild(document.createElement('br'));
                                        }
                                    }
                                } else {
                                    botMessageContainer.appendChild(document.createTextNode(rawToken));
                                }
                                document.getElementById('chatMessages').scrollTop = document.getElementById('chatMessages').scrollHeight;
                            } else if (parsed.error) {
                                let errorHtml = `<div class="alert alert-danger p-2 small mt-2"><b>AI Error:</b> \${parsed.error}`;
                                if (parsed.details) {
                                    errorHtml += `<div class="mt-1 opacity-75" style="font-size: 0.8em; font-family: monospace;">Details: \${parsed.details}</div>`;
                                }
                                errorHtml += `</div>`;
                                botMessageContainer.innerHTML += errorHtml;
                                removeTyping();
                                return;
                            }
                        } catch (err) {
                            console.warn("SSE JSON parse error", err, dataStr);
                        }
                    }
                }
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

    function checkStructuredJson(fullText, containerNode) {
        fullText = fullText.trim();
        if (fullText.startsWith('{') && fullText.endsWith('}')) {
            try {
                const data = JSON.parse(fullText);
                if (data.actionType === 'BOOKING_CONFIRM') {
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
                } else if (data.actionType === 'PAYMENT_INFO') {
                    const details = data.data || {};
                    const html = `
                        <div class="mb-2">\${data.message || 'Đặt vé thành công!'}</div>
                        <div class="action-card payment-card">
                            <div class="payment-header">
                                <i class="fa-solid fa-file-invoice-dollar me-2"></i>Chi tiết hoá đơn #\${details.invoiceId}
                            </div>
                            <div class="payment-body">
                                <div class="small mb-1"><strong>Phim:</strong> \${details.movieName || 'N/A'}</div>
                                <div class="small mb-1"><strong>Ghế:</strong> \${details.seats || 'N/A'}</div>
                                <div class="h5 text-success my-2">Tổng: \${Number(details.total).toLocaleString()} VNĐ</div>
                                <button class="btn btn-success w-100 py-2 fw-bold" onclick="payNow(\${details.invoiceId})">
                                    THANH TOÁN NGAY <i class="fa-solid fa-credit-card ms-1"></i>
                                </button>
                            </div>
                        </div>
                    `;
                    containerNode.innerHTML = html;
                } else if (data.actionType === 'LOGIN_SUCCESS' || data.actionType === 'REGISTER_SUCCESS') {
                    // Tự động reload trang sau 2 giây để cập nhật trạng thái đăng nhập hệ thống
                    setTimeout(() => {
                        const url = new URL(window.location.href);
                        url.searchParams.set('chatRestored', 'true');
                        window.location.href = url.toString();
                    }, 2000);
                }
            } catch (e) {
                // Not valid JSON
            }
        }
    }

    // --- GEN UI WIDGET RENDERER ---
    function renderWidget(widgetData, containerNode) {
        console.log("Rendering side-channel widget:", widgetData.actionType);
        
        const widgetWrapper = document.createElement('div');
        widgetWrapper.className = 'w-100 mt-2';
        
        if (widgetData.actionType === 'LOGIN_FORM') {
            widgetWrapper.innerHTML = `
                <div class="widget-login">
                    <h6 class="text-white mb-2"><i class="fa-solid fa-user-lock me-2"></i>Đăng Nhập</h6>
                    <input type="text" id="widget-login-phone" class="form-control form-control-sm" placeholder="Số điện thoại" autocomplete="tel">
                    <input type="password" id="widget-login-pass" class="form-control form-control-sm" placeholder="Mật khẩu" autocomplete="current-password">
                    <button class="btn btn-primary btn-sm w-100 mt-1" onclick="submitWidgetLogin()">Đăng Nhập</button>
                </div>
            `;
            // Gắn vào DOM
            containerNode.appendChild(widgetWrapper);
        }
        else if (widgetData.actionType === 'SEAT_SELECTION') {
            const seats = widgetData.seats || [];
            let seatHtml = '';
            seats.forEach(s => {
                const sClass = s.status === 'BOOKED' ? 'booked' : 'available';
                seatHtml += `<div class="seat-icon \${sClass}" data-code="\${s.code}" data-price="\${s.price}" onclick="toggleSeatSelection(this)">\${s.code}</div>`;
            });
            
            widgetWrapper.innerHTML = `
                <div class="widget-seat-map" id="seat-map-\${widgetData.showtimeId}">
                    <div class="screen-curve">Màn Hình</div>
                    <div class="seat-grid">
                        \${seatHtml}
                    </div>
                    <div class="widget-footer">
                        <div class="small text-white">
                            Tổng: <b id="seat-total-\${widgetData.showtimeId}" class="text-success">0 đ</b>
                            <div class="text-white-50" style="font-size:10px;" id="seat-list-\${widgetData.showtimeId}">Chưa chọn ghế</div>
                        </div>
                        <button class="btn btn-primary btn-sm" onclick="submitSeatSelection(\${widgetData.showtimeId})">Xác Nhận</button>
                    </div>
                </div>
            `;
            containerNode.appendChild(widgetWrapper);
        }
        else if (widgetData.actionType === 'PAYMENT_INFO') {
            const details = widgetData.data || widgetData; 
            widgetWrapper.innerHTML = `
                <div class="action-card payment-card">
                    <div class="payment-header">
                        <i class="fa-solid fa-file-invoice-dollar me-2"></i>Chi tiết hoá đơn #\${details.invoiceId}
                    </div>
                    <div class="payment-body">
                        <div class="small mb-1"><strong>Phim:</strong> \${details.movieName || 'N/A'}</div>
                        <div class="small mb-1"><strong>Ghế:</strong> \${details.seats || 'N/A'}</div>
                        <div class="h5 text-success my-2">Tổng: \${Number(details.total).toLocaleString()} VNĐ</div>
                        <button class="btn btn-success w-100 py-2 fw-bold" onclick="payNow(\${details.invoiceId})">
                            THANH TOÁN NGAY <i class="fa-solid fa-credit-card ms-1"></i>
                        </button>
                    </div>
                </div>
            `;
            containerNode.appendChild(widgetWrapper);
        }
        else if (widgetData.actionType === 'LOGIN_SUCCESS') {
            console.log("Login success widget received. Reloading page in 2s...");
            setTimeout(() => {
                const url = new URL(window.location.href);
                url.searchParams.set('chatRestored', 'true');
                window.location.href = url.toString();
            }, 2000);
        }
        
        document.getElementById('chatMessages').scrollTop = document.getElementById('chatMessages').scrollHeight;
    }

    // --- WIDGET EVENT HANDLERS ---
    window.submitWidgetLogin = function() {
        const phone = document.getElementById('widget-login-phone').value;
        const pass = document.getElementById('widget-login-pass').value;
        if(phone && pass) {
            executeSilentAction('Login: SĐT ' + phone + ', Mật khẩu: ' + pass);
            document.getElementById('widget-login-phone').parentElement.innerHTML = '<div class="text-success small text-center"><i class="fa-solid fa-spinner fa-spin me-2"></i>Đang xử lý...</div>';
        }
    };

    window.toggleSeatSelection = function(el) {
        if(el.classList.contains('booked')) return;
        el.classList.toggle('selected');
        
        // Locate component wrapper to calculate totals
        const mapContainer = el.closest('.widget-seat-map');
        const showtimeId = mapContainer.id.replace('seat-map-', '');
        
        const selectedEls = mapContainer.querySelectorAll('.seat-icon.selected');
        let total = 0;
        let codes = [];
        
        selectedEls.forEach(s => {
            total += parseFloat(s.getAttribute('data-price'));
            codes.push(s.getAttribute('data-code'));
        });
        
        document.getElementById('seat-total-' + showtimeId).innerText = total.toLocaleString() + ' đ';
        document.getElementById('seat-list-' + showtimeId).innerText = codes.length ? codes.join(', ') : 'Chưa chọn ghế';
    };

    window.submitSeatSelection = function(showtimeId) {
        const mapContainer = document.getElementById('seat-map-' + showtimeId);
        const selectedEls = mapContainer.querySelectorAll('.seat-icon.selected');
        if(selectedEls.length === 0) {
           alert("Vui lòng chọn ít nhất một ghế!");
           return;
        }
        
        let total = 0;
        let codes = [];
        selectedEls.forEach(s => {
            total += parseFloat(s.getAttribute('data-price'));
            codes.push(s.getAttribute('data-code'));
        });
        
        // Gửi lệnh xử lý ẩn lên cho chatbot
        executeSilentAction(`Tôi muốn đặt suất chiếu ID \${showtimeId}, các ghế đã chọn là: \${codes.join(', ')}, tổng số tiền là: \${total}`);
        mapContainer.innerHTML = '<div class="text-success small text-center p-2"><i class="fa-solid fa-check me-1"></i>Đã ghi nhận lựa chọn. Đang xử lý giao dịch...</div>';
    };

    async function executeSilentAction(message) {
        const input = document.getElementById('chatInput');
        input.value = message;
        document.getElementById('chatForm').dispatchEvent(new Event('submit'));
    }

    window.payNow = async function(invoiceId) {
        console.log("Initiating payment for invoice #", invoiceId);
        try {
            const button = event.target.closest('button');
            const originalContent = button.innerHTML;
            button.disabled = true;
            button.innerHTML = '<i class="fa-solid fa-spinner fa-spin me-2"></i>Đang khởi tạo...';
            
            const resp = await fetch(`\${chatContextPath}/ajaxServlet?invoiceId=\${invoiceId}`, { method: 'POST' });
            const result = await resp.json();
            
            if (result.code === '00' && result.data) {
                window.location.href = result.data;
            } else {
                throw new Error(result.message || 'Lỗi khởi tạo thanh toán');
            }
        } catch (e) {
            console.error("Payment error:", e);
            alert("Không thể khởi tạo thanh toán: " + e.message);
        }
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
                isHistoryLoaded = true;
            }
        } catch(e) {
            console.error('Reset failed', e);
        }
    }

    // Tự động mở chat nếu vừa phục hồi từ Login
    window.addEventListener('load', () => {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('chatRestored')) {
            toggleChat();
            // Thêm tin nhắn thông báo
            setTimeout(() => {
                appendMessage('bot', 'Chào mừng trở lại! Tôi đã khôi phục lại cuộc hội thoại của chúng ta.');
            }, 500);
        }
    });
</script>
