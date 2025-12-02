using Lab13and14.Data;
using Lab13and14.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace MyApi.Controllers
{
    public class RegisterRequest
    {
        public string Email { get; set; }
        public string MatKhau { get; set; }
    }

    public class LoginRequest
    {
        public string Email { get; set; }
        public string MatKhau { get; set; }
    }

    [Route("api/[controller]")]
    [ApiController]
    public class NguoiDungController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public NguoiDungController(ApplicationDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<NguoiDung>>> GetAll()
        {
            var list = await _context.NguoiDungs
                                     .OrderByDescending(x => x.NgayTao)
                                     .ToListAsync();
            return Ok(list);
        }

        [HttpGet("{id:int}")]
        public async Task<ActionResult<NguoiDung>> GetById(int id)
        {
            var user = await _context.NguoiDungs.FindAsync(id);
            if (user == null)
            {
                return NotFound();
            }
            return Ok(user);
        }

        [HttpGet("by-email")]
        public async Task<ActionResult<NguoiDung>> GetByEmail([FromQuery] string email)
        {
            if (string.IsNullOrWhiteSpace(email))
                return BadRequest("Email is required.");

            var user = await _context.NguoiDungs
                                     .FirstOrDefaultAsync(x => x.Email == email);
            if (user == null)
                return NotFound();

            return Ok(user);
        }


        [HttpPost("register")]
        public async Task<ActionResult> Register([FromBody] RegisterRequest request)
        {
            if (request == null || string.IsNullOrWhiteSpace(request.Email) || string.IsNullOrWhiteSpace(request.MatKhau))
            {
                return BadRequest("Email và mật khẩu không được để trống.");
            }

            var existed = await _context.NguoiDungs
                                        .AnyAsync(x => x.Email == request.Email);
            if (existed)
            {
                return Conflict("Email đã tồn tại, vui lòng dùng email khác.");
            }

            var user = new NguoiDung
            {
                Email = request.Email.Trim(),
                MatKhau = request.MatKhau,
                NgayTao = DateTime.Now
            };

            _context.NguoiDungs.Add(user);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetById), new { id = user.Id }, user);
        }

        [HttpPost("login")]
        public async Task<ActionResult> Login([FromBody] LoginRequest request)
        {
            if (request == null || string.IsNullOrWhiteSpace(request.Email) || string.IsNullOrWhiteSpace(request.MatKhau))
            {
                return BadRequest("Email và mật khẩu không được để trống.");
            }

            var user = await _context.NguoiDungs
                                     .FirstOrDefaultAsync(x => x.Email == request.Email);

            if (user == null)
            {
                return Unauthorized("Email không tồn tại.");
            }

            if (user.MatKhau != request.MatKhau)
            {
                return Unauthorized("Mật khẩu không đúng.");
            }

            return Ok(new
            {
                Message = "Đăng nhập thành công.",
                UserId = user.Id,
                Email = user.Email,
                NgayTao = user.NgayTao
            });
        }
    }
}
