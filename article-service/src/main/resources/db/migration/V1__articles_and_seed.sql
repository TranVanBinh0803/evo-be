CREATE TABLE IF NOT EXISTS articles (
    id UUID PRIMARY KEY,
    title VARCHAR(180) NOT NULL,
    slug VARCHAR(220) NOT NULL,
    summary VARCHAR(500) NOT NULL,
    content TEXT NOT NULL,
    image_url VARCHAR(1000) NOT NULL,
    author VARCHAR(120) NOT NULL,
    tag VARCHAR(80) NOT NULL,
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    read_minutes INTEGER NOT NULL,
    published_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_article_slug ON articles(slug);

INSERT INTO articles (id, title, slug, summary, content, image_url, author, tag, featured,
                      read_minutes, published_at, created_at, updated_at)
VALUES
('11111111-1111-4111-8111-111111111111', 'Gợi ý chọn nội thất cho căn hộ nhỏ',
 'goi-y-chon-noi-that-cho-can-ho-nho',
 'Ưu tiên đồ đa năng, gam màu sáng và bố cục thoáng để căn hộ nhỏ vẫn đầy đủ tiện nghi.',
 'Không gian nhỏ cần ưu tiên sản phẩm đúng tỷ lệ, có nhiều công năng và giữ được lối đi thông thoáng. Hãy bắt đầu từ các món thiết yếu rồi bổ sung trang trí sau.',
 '/assets/img/news-1.webp', 'Evo Nội Thất', 'Căn hộ', TRUE, 5,
 CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('22222222-2222-4222-8222-222222222222', 'Xu hướng trang trí phòng khách hiện đại',
 'xu-huong-trang-tri-phong-khach-hien-dai',
 'Phòng khách hiện đại chuộng đường nét gọn, chất liệu tự nhiên và điểm nhấn ánh sáng ấm.',
 'Một phòng khách hiện đại không cần quá nhiều đồ. Các đường nét sạch, vật liệu tự nhiên và ánh sáng nhiều lớp tạo nên cảm giác ấm áp mà vẫn gọn gàng.',
 '/assets/img/news-2.webp', 'Evo Nội Thất', 'Xu hướng', TRUE, 6,
 CURRENT_TIMESTAMP - INTERVAL '2 day', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('33333333-3333-4333-8333-333333333333', 'Cách bảo quản đồ gỗ bền đẹp',
 'cach-bao-quan-do-go-ben-dep',
 'Giữ đồ gỗ tránh nắng gắt, lau bằng khăn mềm và xử lý độ ẩm để sản phẩm luôn bền màu.',
 'Đồ gỗ nên được đặt tránh nguồn nhiệt và nắng trực tiếp. Vệ sinh định kỳ bằng khăn mềm hơi ẩm, sau đó lau khô để hạn chế cong vênh và bạc màu.',
 '/assets/img/news-3.webp', 'Evo Nội Thất', 'Kinh nghiệm', TRUE, 4,
 CURRENT_TIMESTAMP - INTERVAL '3 day', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('44444444-4444-4444-8444-444444444444', 'Thiết kế góc làm việc tại nhà',
 'thiet-ke-goc-lam-viec-tai-nha',
 'Một chiếc bàn vừa tầm, ghế thoải mái và ánh sáng tốt giúp góc làm việc hiệu quả hơn.',
 'Chọn vị trí có ánh sáng tự nhiên, bàn đủ sâu cho màn hình và ghế hỗ trợ tư thế. Một kệ nhỏ giúp mặt bàn luôn thông thoáng trong ngày làm việc.',
 '/assets/img/news-4.webp', 'Evo Nội Thất', 'Không gian', FALSE, 5,
 CURRENT_TIMESTAMP - INTERVAL '4 day', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (slug) DO NOTHING;
