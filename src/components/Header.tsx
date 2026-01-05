import { Link, useLocation } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { useState } from 'react';
import { Menu, X, Briefcase, User, LogIn } from 'lucide-react';

export function Header() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const location = useLocation();

  const isActive = (path: string) => location.pathname === path;

  const navLinks = [
    { href: '/', label: 'Home' },
    { href: '/jobs', label: 'Browse Jobs' },
    { href: '/categories', label: 'Categories' },
    { href: '/about', label: 'About' },
  ];

  return (
    <header className="sticky top-0 z-50 w-full border-b border-border bg-card/95 backdrop-blur supports-[backdrop-filter]:bg-card/80">
      <div className="container mx-auto px-4">
        <div className="flex h-16 items-center justify-between">
          {/* Logo */}
          <Link to="/" className="flex items-center gap-2 group">
            <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary text-primary-foreground transition-transform group-hover:scale-105">
              <Briefcase className="h-5 w-5" />
            </div>
            <span className="text-xl font-bold text-foreground">
              Online<span className="text-accent">Jobs</span>Hub
            </span>
          </Link>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex items-center gap-1">
            {navLinks.map((link) => (
              <Link
                key={link.href}
                to={link.href}
                className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
                  isActive(link.href)
                    ? 'bg-accent/10 text-accent'
                    : 'text-muted-foreground hover:text-foreground hover:bg-muted'
                }`}
              >
                {link.label}
              </Link>
            ))}
          </nav>

          {/* Desktop Auth Buttons */}
          <div className="hidden md:flex items-center gap-3">
            <Button variant="ghost" asChild>
              <Link to="/login">
                <LogIn className="h-4 w-4" />
                Sign In
              </Link>
            </Button>
            <Button variant="cta" asChild>
              <Link to="/register">
                <User className="h-4 w-4" />
                Post a Job
              </Link>
            </Button>
          </div>

          {/* Mobile Menu Button */}
          <button
            className="md:hidden p-2 rounded-lg hover:bg-muted"
            onClick={() => setIsMenuOpen(!isMenuOpen)}
            aria-label="Toggle menu"
          >
            {isMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
          </button>
        </div>

        {/* Mobile Navigation */}
        {isMenuOpen && (
          <nav className="md:hidden py-4 border-t border-border animate-fade-in">
            <div className="flex flex-col gap-2">
              {navLinks.map((link) => (
                <Link
                  key={link.href}
                  to={link.href}
                  className={`px-4 py-3 rounded-lg text-sm font-medium transition-colors ${
                    isActive(link.href)
                      ? 'bg-accent/10 text-accent'
                      : 'text-muted-foreground hover:text-foreground hover:bg-muted'
                  }`}
                  onClick={() => setIsMenuOpen(false)}
                >
                  {link.label}
                </Link>
              ))}
              <div className="flex flex-col gap-2 mt-4 pt-4 border-t border-border">
                <Button variant="outline" asChild className="justify-center">
                  <Link to="/login">Sign In</Link>
                </Button>
                <Button variant="cta" asChild className="justify-center">
                  <Link to="/register">Post a Job</Link>
                </Button>
              </div>
            </div>
          </nav>
        )}
      </div>
    </header>
  );
}
