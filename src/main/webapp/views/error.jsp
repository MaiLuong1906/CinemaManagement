<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Lỗi</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <style>
            .error-container {
                max-width: 600px;
                margin: 80px auto;
                padding: 40px;
                text-align: center;
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            }

            .error-container h1 {
                color: #e74c3c;
                font-size: 28px;
                margin-bottom: 16px;
            }

            .error-container .error-message {
                color: #555;
                font-size: 16px;
                margin-bottom: 24px;
                padding: 16px;
                background: #fef2f2;
                border: 1px solid #fecaca;
                border-radius: 8px;
            }

            .error-container .btn-back {
                display: inline-block;
                padding: 10px 24px;
                background: #3498db;
                color: #fff;
                text-decoration: none;
                border-radius: 6px;
                font-size: 14px;
                transition: background 0.3s;
            }

            .error-container .btn-back:hover {
                background: #2980b9;
            }
        </style>
    </head>

    <body>
        <div class="error-container">
            <h1>⚠️ Đã xảy ra lỗi</h1>

            <div class="error-message">
                <%= request.getAttribute("error") !=null ? request.getAttribute("error")
                    : "Đã có lỗi xảy ra. Vui lòng thử lại sau." %>
            </div>

            <a href="${pageContext.request.contextPath}/home" class="btn-back">← Về trang chủ</a>
            &nbsp;
            <a href="javascript:history.back()" class="btn-back" style="background: #95a5a6;">← Quay lại</a>
        </div>
    </body>

    </html>