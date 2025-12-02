using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.EntityFrameworkCore;

namespace Lab13and14.Models;

[Table("NguoiDung")]
[Index("Email", Name = "UQ__NguoiDun__A9D10534D6C26598", IsUnique = true)]
public partial class NguoiDung
{
    [Key]
    public int Id { get; set; }

    [StringLength(255)]
    public string Email { get; set; } = null!;

    [StringLength(255)]
    public string MatKhau { get; set; } = null!;

    [Column(TypeName = "datetime")]
    public DateTime NgayTao { get; set; }
}
