import { Search, MapPin, Filter } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export function SearchBar() {
  const [keyword, setKeyword] = useState('');
  const [location, setLocation] = useState('');
  const navigate = useNavigate();

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    const params = new URLSearchParams();
    if (keyword) params.set('search', keyword);
    if (location) params.set('location', location);
    navigate(`/jobs?${params.toString()}`);
  };

  return (
    <form onSubmit={handleSearch} className="w-full">
      <div className="flex flex-col md:flex-row gap-3 p-2 bg-card rounded-2xl border border-border shadow-elegant">
        {/* Keyword Search */}
        <div className="flex-1 relative">
          <Search className="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-muted-foreground" />
          <Input
            type="text"
            placeholder="Job title, keywords, or company"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            className="pl-12 h-12 border-0 bg-transparent text-base placeholder:text-muted-foreground focus-visible:ring-0"
          />
        </div>

        {/* Divider */}
        <div className="hidden md:block w-px bg-border" />

        {/* Location */}
        <div className="flex-1 relative">
          <MapPin className="absolute left-4 top-1/2 -translate-y-1/2 h-5 w-5 text-muted-foreground" />
          <Input
            type="text"
            placeholder="Remote, City, or Country"
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            className="pl-12 h-12 border-0 bg-transparent text-base placeholder:text-muted-foreground focus-visible:ring-0"
          />
        </div>

        {/* Submit */}
        <Button type="submit" variant="cta" size="lg" className="md:px-8">
          <Search className="h-5 w-5" />
          <span className="hidden sm:inline">Search Jobs</span>
        </Button>
      </div>
    </form>
  );
}
